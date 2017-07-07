package com.fom.msesoft.fomapplication.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fom.msesoft.fomapplication.fragment.DegreeFragment;
import com.fom.msesoft.fomapplication.fragment.ActivityFragment;

import com.fom.msesoft.fomapplication.fragment.DegreeFragment_;
import com.fom.msesoft.fomapplication.fragment.ProfileFragment;
import com.fom.msesoft.fomapplication.fragment.ProfileFragment_;
import com.fom.msesoft.fomapplication.fragment.ActivityFragment_;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MainPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DegreeFragment tab1 = new DegreeFragment_();
                return tab1;
            case 1:
                ProfileFragment tab2 = new ProfileFragment_();
                return tab2;
            case 2:
                ActivityFragment tab3 = new ActivityFragment_();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}