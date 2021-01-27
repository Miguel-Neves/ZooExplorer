package com.cm.zooexplorer;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.collection.ArraySet;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cm.zooexplorer.R;
import com.cm.zooexplorer.models.Habitat;
import com.cm.zooexplorer.viewmodel.HabitatViewModel;

import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private Set<String> unlockedHabitats;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        unlockedHabitats = new ArraySet<>();

        HabitatViewModel habitatViewModel = new ViewModelProvider(this).get(HabitatViewModel.class);
        habitatViewModel.getHabitatsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Habitat>>() {
            @Override
            public void onChanged(List<Habitat> habitats) {
                SharedPreferences prefs = getContext().getSharedPreferences(HabitatsFragment.PREFERENCES_NAME, MODE_PRIVATE);
                unlockedHabitats = new ArraySet<>(prefs.getStringSet(HabitatsFragment.UNLOCKED_HABITATS, unlockedHabitats));
                String contTxt;
                if (habitats.size() > unlockedHabitats.size())
                    contTxt = unlockedHabitats.size() + " / " + habitats.size();
                else
                    contTxt = getString(R.string.completed);
                ((TextView) rootView.findViewById(R.id.textUnlockedContent)).setText(contTxt);
                ((ProgressBar) rootView.findViewById(R.id.habitatsProgress)).setMax(habitats.size());
                ((ProgressBar) rootView.findViewById(R.id.habitatsProgress)).setProgress(unlockedHabitats.size());
            }
        });

        return rootView;
    }

    public static ProfileFragment newInstance(){
        return new ProfileFragment();
    }
}
