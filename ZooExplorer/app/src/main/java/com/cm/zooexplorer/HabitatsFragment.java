package com.cm.zooexplorer;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.VibrationEffect;
import android.os.Vibrator;
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
    private static final String TAG = "ZooExplorer-HABITATS";
    public static final String UNLOCKED_HABITATS = "UNLOCKED_HABITATS";
    public static final String PREFERENCES_NAME = "com.cm.zooexplorer.UNLOCKED_HABITATS";
    public static int HABITAT_REQUEST_CODE = 1;
    //private static HabitatsFragment fragment;
    private List<Habitat> habitatList;
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

        habitatList = new LinkedList<>();
        habitatViewModel = new ViewModelProvider(this).get(HabitatViewModel.class);
        habitatViewModel.getHabitatsLiveData().observe(this, new Observer<List<Habitat>>() {
            @Override
            public void onChanged(List<Habitat> habitats) {
                habitatList = habitats;
                adapter.setHabitats(habitats);
                if (!habitatList.isEmpty())
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

        adapter = new HabitatsAdapter(habitatList);
        habitatsRecyclerView.setAdapter(adapter);
        habitatsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.habitatsNumCol)));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), QrCodeActivity.class);
                try {
                    startActivityForResult(intent, HABITAT_REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), getString(R.string.qrCaptureError), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Could not initiate the QR code capture. Error: ", e);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!habitatList.isEmpty())
            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == HABITAT_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            try {
                // Get data read from the QR code
                String habitatId = data.getStringExtra(QrCodeActivity.HABITAT_ID);

                // Get set of unlocked habitats saved locally
                SharedPreferences prefs = getContext().getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
                unlockedHabitats = new ArraySet<>(prefs.getStringSet(UNLOCKED_HABITATS, unlockedHabitats));

                // Verify if the data represents a valid habitat ID and proceed accordingly
                //else if (habitatList.stream().anyMatch(e -> e.getId().equals(habitatId)))
                if (!isValidHabitat(habitatId))
                    Toast.makeText(getContext(), getString(R.string.invalidHabitat), Toast.LENGTH_LONG).show();
                else {
                    habitatsRecyclerView.scrollToPosition(Integer.parseInt(habitatId) - 1);
                    if (unlockedHabitats.contains(habitatId))
                        Toast.makeText(getContext(), getString(R.string.habitatAlreadyUnlocked), Toast.LENGTH_LONG).show();
                    else {
                        SharedPreferences.Editor editor = prefs.edit();
                        unlockedHabitats.add(habitatId);
                        editor.putStringSet(UNLOCKED_HABITATS, unlockedHabitats);
                        editor.apply();
                        adapter.notifyItemChanged(Integer.parseInt(habitatId) - 1);
                        if (habitatList.size() > unlockedHabitats.size())
                            Toast.makeText(getContext(), getString(R.string.habitatUnlocked, habitatId), Toast.LENGTH_LONG).show();
                        else {
                            Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                            if (vibrator != null)
                                vibrator.vibrate(500);
                            Toast.makeText(getContext(), getString(R.string.allHabitatsUnlocked), Toast.LENGTH_LONG).show();
                        }
                        Intent intent = new Intent(getContext(), HabitatProfileActivity.class);
                        intent.putExtra(HabitatsAdapter.HABITAT_IDENTIFIER, habitatId);
                        getContext().startActivity(intent);
                    }
                }
            } catch (NullPointerException e) {
                Toast.makeText(getContext(), getString(R.string.qrCodeError), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error reading QR code: ", e);
            }
        }
    }

    private boolean isValidHabitat(String id) {
        for (Habitat habitat : habitatList) {
            if (habitat.getId().equals(id))
                return true;
        }
        return false;
    }

    public static HabitatsFragment newInstance(){
        return new HabitatsFragment(); //fragment==null ? new HabitatsFragment() : fragment;
    }
}
