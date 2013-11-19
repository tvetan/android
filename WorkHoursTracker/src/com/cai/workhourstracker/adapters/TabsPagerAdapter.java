package com.cai.workhourstracker.adapters;

import com.cai.workhourstracker.EntriesFragment;
import com.cai.workhourstracker.JobsFragment;
import com.cai.workhourstracker.PayPeriodsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
 
public class TabsPagerAdapter extends FragmentStatePagerAdapter {
 
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new JobsFragment();
        case 1:
            // Games fragment activity
            return new EntriesFragment();
        case 2:
            // Movies fragment activity
            return new PayPeriodsFragment();
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
}
