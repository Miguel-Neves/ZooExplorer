package com.cm.zooexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
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

                verifyInternetConnection();

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

    @Override
    protected void onResume() {
        super.onResume();
        verifyInternetConnection();
    }

    private void verifyInternetConnection() {
        boolean connected;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            connected = cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (NullPointerException e) {
            connected = false;
        }
        if (!connected)
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(getString(R.string.internetRequiredTitle))
                    .setMessage(getString(R.string.internetRequiredMessage))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .show();
    }

    public void openFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.tab_page_container, fragment);
        transaction.commit();
    }
}
