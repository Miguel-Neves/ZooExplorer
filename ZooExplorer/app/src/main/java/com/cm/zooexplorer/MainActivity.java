package com.cm.zooexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);

        // init bottom navigation bar with habitats tab selected
        bottomNavigationBar.setSelectedItemId(R.id.habitats_page);
        openFragment(HabitatsFragment.newInstance());

        bottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.map_page:
                        openFragment(MapFragment.newInstance());
                        break;
                    case R.id.habitats_page:
                        openFragment(HabitatsFragment.newInstance());
                        break;
                    case R.id.profile_page:
                        openFragment(ProfileFragment.newInstance());
                        break;
                }

                return true;
            }
        });
    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.tab_page_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
