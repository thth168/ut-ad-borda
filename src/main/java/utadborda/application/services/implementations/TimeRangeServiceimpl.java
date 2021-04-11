package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.TimeRange;
import utadborda.application.services.DAO.TimeRangeRepo;
import utadborda.application.services.TimeRangeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimeRangeServiceimpl implements TimeRangeService {
    private TimeRangeRepo timeRangeRepo;
    @Autowired
    public TimeRangeServiceimpl(
        TimeRangeRepo timeRangeRepo
    ){
        this.timeRangeRepo = timeRangeRepo;
    }
    @Override
    public TimeRange addTimeRange(TimeRange timeRange) {
        return timeRangeRepo.save(timeRange);
    }

    @Override
    public List<TimeRange> addTimeRanges(List<TimeRange> timeRange) {
        List<TimeRange> toAdd = new ArrayList<>();
        Map<String, TimeRange> uniqueValues = new HashMap<>();
        timeRange.forEach((TimeRange tr) -> {
            String identifier = tr.getOpenTime() + tr.getCloseTime() + tr.getWeekDay() + tr.isHoliday();
            TimeRange match = uniqueValues.get(identifier);
            if (match != null) {
                match.addRestaurant(tr.getRestaurant().get(0));
            } else {
                uniqueValues.put(identifier, tr);
            }
        });
        for (TimeRange tr: uniqueValues.values()) {
            TimeRange candidate = tr;
            TimeRange existing  = timeRangeRepo.getTimeRangeByOpenTimeAndCloseTimeAndWeekDayAndHoliday(
                    candidate.getOpenTime(),
                    candidate.getCloseTime(),
                    candidate.getWeekDay(),
                    candidate.isHoliday()
            );
            if (existing != null) candidate = existing;
            toAdd.add(candidate);
        }
        return timeRangeRepo.saveAll(toAdd);
    }
}
