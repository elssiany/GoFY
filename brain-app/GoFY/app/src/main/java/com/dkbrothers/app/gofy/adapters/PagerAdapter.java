package com.dkbrothers.app.gofy.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dkbrothers.app.gofy.fragments.HomeFragment;
import com.dkbrothers.app.gofy.fragments.ScanSystemFragment;
import com.dkbrothers.app.gofy.fragments.UserActivityFragment;

/**
 * Created by kens on 12/10/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {


    private int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new UserActivityFragment();
            case 2:
                return new UserActivityFragment();
            default:
                return new ScanSystemFragment();
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}