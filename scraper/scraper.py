# Mikolaj Cymcyk (mic5@hi.is) 2020.10.28
# Google Places API aided restaurant scrape of Iceland

# Required imports for Google Places API scrape
from bs4 import BeautifulSoup
import json
import requests
import math
import time
import os


# Iceland's extreme points
EXTREMITIES = {      # Place        Latitude    Longitude
    "N":  67.135833, # Kolbeinsey  (67.135833, -18.684167)
    "E": -13.233333, # Hvalbakur   (64.583333, -13.233333)
    "S":  63.283333, # Surtsey     (63.283333, -20.583333)
    "W": -24.533333  # Bjargtangar (65.500000, -24.533333)
}

# Distance in m per °1 change in position in the vicinity of Iceland
LAT_SCALE = 111195 # Distance per 1° change in Latitude
LNG_SCALE = 50480  # Distance per 1° change in Longitude @ 63° Latitude

# Iceland's height and width in degrees
LAT = abs(EXTREMITIES['N'] - EXTREMITIES['S']) # Southern to Nothermost point
LNG = abs(EXTREMITIES['W'] - EXTREMITIES['E']) # Western to Easternmost point

# Offset for Iceland's Latitude positioning to square the area
LAT_OFFSET = LAT * (1 / ((LAT * LAT_SCALE) / (LNG * LNG_SCALE)) - 1) / 2
EXTREMITIES['N'] = EXTREMITIES['N'] + LAT_OFFSET # North offset
EXTREMITIES['S'] = EXTREMITIES['S'] - LAT_OFFSET # South offset

# Google API variables
# Basic Data (0.0$ / 1000)
# Contact Data (3.0$ / 1000)
# Atmosphere Data (5.0$ / 1000)
# Places Photo (7.0$ / 1000)
API_KEY        = ""
API_NEARBY     = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
API_DETAILS    = "https://maps.googleapis.com/maps/api/place/details/json?"
API_PHOTO      = "https://maps.googleapis.com/maps/api/place/photo?"
SKU_BASIC      = "address_component,adr_address,business_status,formatted_address,geometry,icon,name,permanently_closed,photo,place_id,plus_code,type,url,utc_offset,vicinity"
SKU_CONTACT    = "formatted_phone_number,international_phone_number,opening_hours,website"
SKU_ATMOSPHERE = "price_level,rating,review,user_ratings_total"
MAX_RADIUS     = 50000         # 50,000 is max supported by Google Places API
MIN_RADIUS     = 200           # Tweak for efficiency
USE_KEYWORD_FILTER = True      # Use keyword filter over type filter
SEARCH_FILTER      = None      # filter to use 
TOKEN_TIMEOUT      = 5         # next_page_token activation wait time
PHOTO_COUNT        = 2         # number of photos to downlaod per location

# Scraper data files
SCRAPER_DATA_LOC    = "scraper/scraper_data.json"
SCRAPER_POINTS_LOC  = "scraper/scraper_points.txt"
SCRAPER_REGIONS_LOC = "scraper/scraper_regions.txt"
PHOTO_FOLDER        = "scraper/photos/"
DATA_FORMAT         = """{"results": [], "ids": []}"""
DATA_FOLDER         = "scraper/data"
SCRAPER_DATA        = {} # Raw data for scraped locations.
SCRAPER_POINTS      = [] # Points marking the scraped areas. Used to display on map
SCRAPER_REGIONS     = [] # Regions marking the scraped data. Used to prevent re-scraping

# Progress printouts
PROGRESS_PRINTOUT       = True     # Enable or disable scraper progress printouts
PROGRESS_PRINTOUT_EVERY = 1        # Prints progress updates every x data points added
PROGRESS_UPDATE_COUNTER = 0        # Counter used to printout progress
PROGRESS_COUNTER        = 0.0      # The progress counter itself, stores progress

# Misc data store
repeats_location = 0      # Count of how many duplicates got found.
repeats_region   = 0      # Count of how many regions were skipped due to previous searches
caught_up        = True   # If the recursive search has caught up with stored data
catchup_region   = None   # last stored region tag for the scraper to catch up to

# Returns Google Places API call results for location at GPS coordinates
# latitude, longitude, within a given radius filtered by an optional filter
# optionally with a keyword search instead of type and also if next_page_token
# is present in returns get all available results, up to 60 with 20 max per page
def get_locations(latitude, longitude, radius, filter=False, keyword_filter=False, full_depth=False):
    query = f"{API_NEARBY}location={latitude},{longitude}&radius={radius}"
    if filter:
        if keyword_filter:
            query += f"&keyword={filter}"
        else:
            query += f"&type={filter}"
    query += f"&key={API_KEY}"
    answer = json.loads(requests.get(query).text)
    if full_depth:
        if 'next_page_token' in answer:
            time.sleep(TOKEN_TIMEOUT)
            answer2 = json.loads(
                requests.get(
                    f"{API_NEARBY}pagetoken={answer['next_page_token']}&key={API_KEY}"
                    ).text
                )
            for item in answer2['results']:
                answer['results'].append(item)
            if 'next_page_token' in answer2:
                time.sleep(TOKEN_TIMEOUT)
                answer3 = json.loads(
                    requests.get(
                        f"{API_NEARBY}pagetoken={answer2['next_page_token']}&key={API_KEY}"
                    ).text
                )
                for item in answer3['results']:
                    answer['results'].append(item)
    return answer

# Returns the hypotenal length in meters for an
# on-map triangle with angular sides of length a, b
def calculate_radius(a, b):
    return math.sqrt(math.pow(a * LAT_SCALE, 2) + math.pow(b * LNG_SCALE, 2))

# Loads saved scraper data, points and regions files into variables from previous runs
def load_data():
    global SCRAPER_DATA, SCRAPER_POINTS, SCRAPER_REGIONS, PROGRESS_COUNTER, catchup_region, caught_up
    if os.path.exists(SCRAPER_DATA_LOC):
        SCRAPER_DATA = json.load(open(SCRAPER_DATA_LOC, "r", encoding="utf-8"))
    else:
        file = open(SCRAPER_DATA_LOC, "x", encoding="utf-8")
        SCRAPER_DATA = json.loads(DATA_FORMAT)
        file.write(json.dumps(SCRAPER_DATA))
        file.close()
    if os.path.exists(SCRAPER_POINTS_LOC):
        file = open(SCRAPER_POINTS_LOC, "r", encoding="utf-8")
        for line in file:
            pos = line.split(',')
            SCRAPER_POINTS.append((float(pos[0]), float(pos[1])))
    else:
        file = open(SCRAPER_POINTS_LOC, "x", encoding="utf-8")
    file.close()
    if os.path.exists(SCRAPER_REGIONS_LOC):
        file = open(SCRAPER_REGIONS_LOC, "r", encoding="utf-8")
        for line in file:
            region = line.replace("\n", "")
            SCRAPER_REGIONS.append(region)
            PROGRESS_COUNTER += (1 / math.pow(4, (len(region) / 2)))
        catchup_region = SCRAPER_REGIONS[len(SCRAPER_REGIONS) - 1]
        caught_up = False
    else:
        file = open(SCRAPER_REGIONS_LOC, "x", encoding="utf-8")
    file.close()
    if PROGRESS_PRINTOUT:
        print(f"{'{:.2f}'.format(PROGRESS_COUNTER * 100)}%")

# saves loaded data, points and regions into their respective files
def save_data(file=''):
    if file:
        json.dump(SCRAPER_DATA, open(file, "w", encoding="utf-8"))
    else:
        json.dump(SCRAPER_DATA, open(SCRAPER_DATA_LOC, "w", encoding="utf-8"))
        file = open(SCRAPER_POINTS_LOC, "w", encoding="utf-8")
        for point in SCRAPER_POINTS:
            file.write(f"{point[0]},{point[1]}\n")
        file.close()
        file = open(SCRAPER_REGIONS_LOC, "w", encoding="utf-8")
        for region in SCRAPER_REGIONS:
            file.write(f"{region}\n")
        file.close()

# Data logger support function for recursive scraper
# logs scraped data into variables, can be saved with the save_data function
def log_data(data, region, N, E, S, W, h, w):
    global SCRAPER_DATA, SCRAPER_POINTS, SCRAPER_REGIONS, PROGRESS_COUNTER, PROGRESS_UPDATE_COUNTER, repeats_location
    if data['status'] != 'OK' and data['status'] != 'ZERO_RESULTS':
        raise Exception("Invalid status code", [data, region, N, E, S, W, h, w])
    for location in data['results']:
        if location['place_id'] in SCRAPER_DATA['ids']:
            repeats_location += 1
        else:
            SCRAPER_DATA['results'].append(location)
            SCRAPER_DATA['ids'].append(location['place_id'])
    points = [(N, W), (N, E), (S, W), (S, E), (S + h, W + w)]
    for point in points:
        if point not in SCRAPER_POINTS:
            SCRAPER_POINTS.append(point)
    SCRAPER_REGIONS.append(region)
    if PROGRESS_PRINTOUT:
        PROGRESS_COUNTER += (1 / math.pow(4, (len(region) / 2)))
        PROGRESS_UPDATE_COUNTER += 1
        if PROGRESS_UPDATE_COUNTER % PROGRESS_PRINTOUT_EVERY == 0:
            print(f"{'{:.2f}'.format(PROGRESS_COUNTER * 100)}%")
            PROGRESS_UPDATE_COUNTER = 0

# Map scraper
# Google Places API aided recursive location scraper
# Scrapes Google API Data for area within N, E, S, W bounds recursively
# splitting into quadrants while the radius exceeds set maximum or
# results within area exceed API return size limit
def scrape(N, E, S, W, region=''):
    global repeats_region, caught_up, catchup_region
    height = abs(N - S)/2
    width = abs(W - E)/2
    if not caught_up:
        if catchup_region == region:
            caught_up = True
            print("Cought up to old data in recursion tree.")
        else:
            if len(catchup_region) < len(region):
                return
            else:
                scrape(N         , E - width, S + height, W        , region + "NW")
                scrape(N         , E        , S + height, W + width, region + "NE")
                scrape(N - height, E - width, S         , W        , region + "SW")
                scrape(N - height, E        , S         , W + width, region + "SE")
    else:
        if region in SCRAPER_REGIONS:
            repeats_region += 1
            return
        radius = calculate_radius(height, width)
        if radius > MAX_RADIUS:
            scrape(N         , E - width, S + height, W        , region + "NW")
            scrape(N         , E        , S + height, W + width, region + "NE")
            scrape(N - height, E - width, S         , W        , region + "SW")
            scrape(N - height, E        , S         , W + width, region + "SE")
        else:
            if radius < MIN_RADIUS:
                data = get_locations(
                    S + height,
                    W + height,
                    radius,
                    filter = SEARCH_FILTER,
                    keyword_filter = USE_KEYWORD_FILTER,
                    full_depth = True
                )
                if len(data['results']) == 60:
                    scrape(N         , E - width, S + height, W        , region + "NW")
                    scrape(N         , E        , S + height, W + width, region + "NE")
                    scrape(N - height, E - width, S         , W        , region + "SW")
                    scrape(N - height, E        , S         , W + width, region + "SE")
                else:
                    log_data(data, region, N, E, S, W, height, width)
            else:
                data = get_locations(
                    S + height,
                    W + width,
                    radius,
                    filter = SEARCH_FILTER,
                    keyword_filter = USE_KEYWORD_FILTER,
                    full_depth = False
                )
                if len(data['results']) == 20:
                    scrape(N         , E - width, S + height, W        , region + "NW")
                    scrape(N         , E        , S + height, W + width, region + "NE")
                    scrape(N - height, E - width, S         , W        , region + "SW")
                    scrape(N - height, E        , S         , W + width, region + "SE")
                else:
                    log_data(data, region, N, E, S, W, height, width)

# Helper function for running the scraper
# Catches exceptions and saves data in case of failure
def run(N, E, S, W):
    try:
        load_data()
        scrape(N, E, S, W)
    except Exception as e:
        print("Exception occured.")
        print("Data dump:")
        print(e)
    finally:
        print("Saving data.")
        save_data()
        print("Data saved.")
        print(f"Total progress: {'{:.2f}'.format(PROGRESS_COUNTER * 100)}%")
        print(f"Number of duplicate locations found {repeats_location}.")
        print(f"Regions skipped due to repeats: {repeats_region}.")

# Helper function for running the scraper, like run
# but runs for all supported types related to food and drink
# dumping into the scraper/data folder
def run_many():
    types = [
        ("bakery"           , False), ("bakery"           , True ),
        ("bar"              , False), ("bar"              , True ), 
        ("cafe"             , False), ("cafe"             , True ),
        ("convenience_store", False), ("convenience store", True ),
        ("food court"       , True ), ("food_court"       , True ),
        ("liquor_store"     , False), ("liquor_store"     , True ),
        ("meal_delivery"    , False), ("meal delivery"    , True ),
        ("meal_takeaway"    , False), ("meal takeaway"    , True ),
        ("night_club"       , False), ("night club"       , True ),
        ("restaurant"       , False), ("restaurant"       , True ),
        ("supermarket"      , False), ("supermarket"      , True )
    ]
    for filter in types:
        try:
            SEARCH_FILTER      = filter[0]
            USE_KEYWORD_FILTER = filter[1]
            scrape(EXTREMITIES['N'], EXTREMITIES['E'], EXTREMITIES['S'], EXTREMITIES['W'])
            save_data(file=f"{DATA_FOLDER}/{SEARCH_FILTER}-{str(USE_KEYWORD_FILTER)}.json")
        except Exception as e:
            print("Exception occured.")
            print("Data dump:")
            print(e)

# Combines all the data scraped by the scraper into a single file
# discarding all duplicates
# WARNING
# API call count for the search using all the types within the function
# within the squared extreme points of iceland will result in around
# twelve-thousand (12.000) calls to the Google Places API resulting in
# MINIMUM 360$ USD in fees at current 30$/1000 calls price
def merge_data():
    data = json.loads(DATA_FORMAT)
    duplicates = 0
    total = 0
    for file_name in os.listdir(DATA_FOLDER):
        file = json.load(open(f"{DATA_FOLDER}/{file_name}", "r", encoding="utf-8"))
        print(f'Going through {file_name}')
        print(f"File entry count: {len(file['results'])}")
        total += len(file['results'])
        for result in file['results']:
            if result['place_id'] not in data['ids']:
                data['results'].append(result)
                data['ids'].append(result['place_id'])
            else:
                duplicates += 1
    print(f'Number of duplicates: {duplicates}.')
    file = open(f"scraper/merged_data.json", "w", encoding="utf-8")
    file.write(json.dumps(data))
    file.close()
    print(f"Number of entries: {len(data['results'])}")
    print(f"Total number of entries in all files: {total}")
    print("Veridying...")
    data = json.load(open(f"scraper/merged_data.json", "r", encoding="utf-8"))
    diff = 0
    for file_name in os.listdir(DATA_FOLDER):
        file = json.load(open(f"{DATA_FOLDER}/{file_name}", "r", encoding="utf-8"))
        for result in file['results']:
            if result['place_id'] not in data['ids']:
                diff += 1
    if diff == 0:
        print("Validated")

# Gets data about Icelandic establishments with a permit
# for the sale of alcoholic beverages from the public government permit list
def get_govt_data():
    raw_site = BeautifulSoup(
        requests.get(
            "https://www.syslumenn.is/thjonusta/utgefin-leyfi/veitinga-og-gististadir/"
        ).text
    )
    html = raw_site.find_all('tr', attrs={'class': 'leyfi-row'})
    data = {"veitingastadir": []}
    for entry in html:
        item = {}
        item['stadur'] = entry.find(attrs={'class': 'stadur'}).text
        item['nickname'] = entry.find(attrs={'class': 'nickname'}).text
        item['gata'] = entry.find(attrs={'class': 'gata'}).text
        item['pnr'] = entry.find(attrs={'class': 'pnr'}).text
        item['gildirTil'] = entry.find(attrs={'class': 'gildirTil'}).text
        item['gististadur'] = entry.find(attrs={'class': 'gististadur'}).text
        item['veitingastadur'] = entry.find(attrs={'class': 'veitingastadur'}).text
        item['tegundleyfis'] = entry.find(attrs={'class': 'tegundleyfis'}).text
        item['utgefandi'] = entry.find(attrs={'class': 'utgefandi'}).text
        item['leyfishafi'] = entry.find(attrs={'class': 'leyfishafi'}).text
        if item['veitingastadur'] != "" and item['veitingastadur'] != "\n"and item['veitingastadur'] != "...":
            data["veitingastadir"].append(item)
    json.dump(data, open(f"scraper/govt_data.json", "w", encoding="utf-8"))

# Filters merged data by type keys
def filter_data(keys=[]):
    if not keys:
        print("No filter keys given.")
        return
    file = json.load(open(f"scraper/merged_data.json", "r", encoding="utf-8"))
    data = json.loads(DATA_FORMAT)
    for location in file['results']:
        if any(key in location['types'] for key in keys):
            if any(key == "permanently_closed" for key in location.keys()):
                #print(location['name'])
                continue
            data['results'].append(location)
            data['ids'].append(location['place_id'])
        else:
            print(location['name'])
    json.dump(data, open("scraper/filtered_data.json", "w", encoding="utf-8"))

# saves location points to file
def map_locations(file):
    data = json.load(open(file, "r", encoding="utf-8"))
    points = []
    for location in data['results']:
        points.append(
            (
                location['geometry']['location']['lat'],
                location['geometry']['location']['lng']
            )
        )
    file = open("scraper/pointmap.txt", "w", encoding="utf-8")
    for point in points:
        file.write(f"{point[0]}, {point[1]}\n")
    file.close()

# Gathers additional data, basic, contact, and atmosphere for each of the
# location ids in the given data file. The ids must represent a google
# maps location, be saved in a json format under the key ids.
def get_additional_location_data(file):
    if not file:
        print("No data source file given.")
        print("Cannot gather additional data.")
        return
    old_data = json.load(open(file, "r", encoding="utf-8"))
    data = json.loads(DATA_FORMAT)
    for id in old_data['ids']:
        print(id)
        query = f"{API_DETAILS}place_id={id}&fields={SKU_BASIC},{SKU_CONTACT},{SKU_ATMOSPHERE}&key={API_KEY}"
        result = json.loads(requests.get(query).text)
        data['results'].append(result['result'])
        data['ids'].append(id)
    json.dump(data, open(f"{file.replace('.json', '')}_complete.json", "w", encoding="utf-8"))

# Prints all keys in data
def get_keys(file):
    data = json.load(open(file, "r", encoding="utf-8"))
    keys = []
    for result in data['results']:
        for key in result.keys():
            if key not in keys:
                keys.append(key)
    return keys

# Prints all tags in data
def get_tags(file):
    data = json.load(open(file, "r", encoding="utf-8"))
    tags = []
    for result in file['results']:
        for tag in result['types']:
            if tag not in tags:
                tags.append(tag)
    return tags

# Downloads photos and saves to photo folder.
def get_photos(file):
    data = json.load(open(file, "r+", encoding="utf-8"))
    for location in data["results"]:
        if "photos" in location:
            i = 0
            while i < PHOTO_COUNT and i < len(location["photos"]):
                photo = location["photos"][i]
                reference = photo["photo_reference"]
                if f"{reference}.png" not in os.listdir(PHOTO_FOLDER):
                    width = photo["width"]
                    height = photo["height"]
                    response = requests.get(f"{API_PHOTO}maxwidth={width}&maxheight={height}&photoreference={reference}&key={API_KEY}")
                    if (response.status_code == 200):
                        with open(f"{PHOTO_FOLDER}{reference}.png", "wb") as f:
                            for chunk in response:
                                f.write(chunk)
                            print(f"saving file:\n{PHOTO_FOLDER}{reference}.png")
                else:
                    print(location["name"])
                    print(f"file already exists:\n{PHOTO_FOLDER}{reference}.png")
                i += 1
get_photos("scraper/merged_data_complete.json")