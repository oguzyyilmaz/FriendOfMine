package com.fom.msesoft.fomapplication.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fom.msesoft.fomapplication.fragment.ProfilePhotoFragment;
import com.fom.msesoft.fomapplication.fragment.ProfilePhotoFragment_;
import com.fom.msesoft.fomapplication.fragment.ProfileSettingsFragment;
import com.fom.msesoft.fomapplication.fragment.ProfileSettingsFragment_;
import com.fom.msesoft.fomapplication.fragment.ProfileNotifyFragment;
import com.fom.msesoft.fomapplication.fragment.ProfileNotifyFragment_;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ProfilePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProfilePhotoFragment tab1 = new ProfilePhotoFragment_();
                return tab1;
            case 1:
                ProfileSettingsFragment tab2 = new ProfileSettingsFragment_();
                return tab2;
            case 2:
                ProfileNotifyFragment tab3 = new ProfileNotifyFragment_();
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