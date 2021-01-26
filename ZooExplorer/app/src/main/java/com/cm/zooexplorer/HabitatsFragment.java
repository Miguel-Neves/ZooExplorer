package com.cm.zooexplorer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cm.zooexplorer.viewmodel.HabitatViewModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.cm.zooexplorer.adapters.HabitatsAdapter;
import com.cm.zooexplorer.models.Habitat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HabitatsFragment extends Fragment {
    public static final String UNLOCKED_HABITATS = "UNLOCKED_HABITATS";
    public static final String PREFERENCES_NAME = "com.cm.zooexplorer.UNLOCKED_HABITATS";
    public static int HABITAT_REQUEST_CODE = 1;
    //private static HabitatsFragment fragment;
    private final List<Habitat> habitats = new LinkedList<>();
    private RecyclerView habitatsRecyclerView;
    private HabitatsAdapter adapter;
    private HabitatViewModel habitatViewModel;
    private ProgressBar progressBar;
    Set<String> unlockedHabitats;

    public HabitatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        habitatViewModel = new ViewModelProvider(this).get(HabitatViewModel.class);
        habitatViewModel.getHabitatsLiveData().observe(this, new Observer<List<Habitat>>() {
            @Override
            public void onChanged(List<Habitat> habitats) {
                adapter.setHabitats(habitats);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        unlockedHabitats = new ArraySet<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_habitats, container, false);

        habitatsRecyclerView = rootView.findViewById(R.id.habitats_recycler_view);
        progressBar = rootView.findViewById(R.id.progress_bar);
        FloatingActionButton fab = rootView.findViewById(R.id.floatingActionButton);

        adapter = new HabitatsAdapter(habitats);
        habitatsRecyclerView.setAdapter(adapter);
        habitatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QrCodeActivity.class);
                startActivityForResult(intent, HABITAT_REQUEST_CODE);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == HABITAT_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                SharedPreferences prefs = getContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                unlockedHabitats = new ArraySet<>(prefs.getStringSet(UNLOCKED_HABITATS, unlockedHabitats));
                SharedPreferences.Editor editor = prefs.edit();

                unlockedHabitats.add(data.getStringExtra(QrCodeActivity.HABITAT_ID));

                editor.putStringSet(UNLOCKED_HABITATS, unlockedHabitats);
                editor.apply();
                adapter.notifyItemChanged(Integer.parseInt(data.getStringExtra(QrCodeActivity.HABITAT_ID))-1);
                habitatsRecyclerView.scrollToPosition(Integer.parseInt(data.getStringExtra(QrCodeActivity.HABITAT_ID))-1);
            }
        }
    }

    public static HabitatsFragment newInstance(){
        return new HabitatsFragment(); //fragment==null ? new HabitatsFragment() : fragment;
    }
}
