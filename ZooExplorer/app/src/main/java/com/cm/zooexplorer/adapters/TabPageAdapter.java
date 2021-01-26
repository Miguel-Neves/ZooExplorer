package com.cm.zooexplorer.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cm.zooexplorer.InfoFragment;
import com.cm.zooexplorer.GalleryFragment;

public class TabPageAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public TabPageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);
        this.numOfTabs = numOfTabs;
    }

    // TODO: change the fragments!
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new InfoFragment();
            case 1: return new GalleryFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
