package com.example.utadborda.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.utadborda.R;
import com.example.utadborda.RestaurantFragment;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private int isAdmin = 1;
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment tabFragment = null;
        switch (position) {
            case 0:
                tabFragment = new RestaurantFragment();
            case 1:
                tabFragment = new RestaurantFragment();
            case 2:
                tabFragment = new RestaurantFragment();
            case 3:
                tabFragment = new RestaurantFragment();
        }
        return tabFragment;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
    @Override
    public int getCount() {
        // Show 3 total pages.
        if(isAdmin == 1 ){
            return 4;
        } else {
            return 3;
        }

    }
}