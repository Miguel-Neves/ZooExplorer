package com.cm.zooexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cm.zooexplorer.adapters.HabitatsAdapter;
import com.cm.zooexplorer.adapters.TabPageAdapter;
import com.cm.zooexplorer.models.Habitat;
import com.cm.zooexplorer.viewmodel.HabitatViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HabitatProfileActivity extends AppCompatActivity {
    private HabitatViewModel habitatViewModel;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView img;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habitat_profile);

        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        img = findViewById(R.id.collapsing_img);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.info_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.gallery_icon));

        Intent intent = getIntent();
        final String habitatId = intent.getStringExtra(HabitatsAdapter.HABITAT_IDENTIFIER);

        collapsingToolbar.setTitle("Habitat " + habitatId);

        habitatViewModel = new ViewModelProvider(this).get(HabitatViewModel.class);
        habitatViewModel.getHabitat(habitatId).observe(this, new Observer<Habitat>() {
            @Override
            public void onChanged(Habitat habitat) {
                img.setImageResource(getImage(habitat.getImageName()));
            }
        });

        final ViewPager viewPager = findViewById(R.id.habitat_pager);
        final TabPageAdapter adapter = new TabPageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("Tab", "---> " + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private int getImage(String imgName){
        return getApplicationContext().getResources().getIdentifier(imgName, "drawable", getApplicationContext().getPackageName());
    }
}
