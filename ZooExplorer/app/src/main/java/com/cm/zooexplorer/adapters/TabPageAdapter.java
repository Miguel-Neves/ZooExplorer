package com.cm.zooexplorer.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cm.zooexplorer.InfoFragment;
import com.cm.zooexplorer.GalleryFragment;

public class TabPageAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;
    private String habitat_id;

    public TabPageAdapter(FragmentManager fm, int numOfTabs, String habitat_id) {
        super(fm, numOfTabs);
        this.numOfTabs = numOfTabs;
        this.habitat_id = habitat_id;
    }

    // TODO: change the fragments!
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new InfoFragment();
            case 1: return new GalleryFragment(habitat_id);
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
