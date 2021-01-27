package com.cm.zooexplorer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cm.zooexplorer.models.Habitat;
import com.cm.zooexplorer.viewmodel.HabitatViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Contains the habitat information view, a list of info about the habitat's species.
 */
public class InfoFragment extends Fragment {

    private String habitatId;
    private Habitat habitat;

    public InfoFragment(String habitatId) {
        this.habitatId = habitatId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        HabitatViewModel habitatViewModel = new ViewModelProvider(this).get(HabitatViewModel.class);
        habitatViewModel.getHabitat(habitatId).observe(getViewLifecycleOwner(), new Observer<Habitat>() {
            @Override
            public void onChanged(Habitat habitat) {
                String femaleSymbol = "<font color=\"#ff1493\"><big><b>♀</b></big></font>\t\t";
                String maleSymbol = "<font color=\"#1E90ff\"><big><b>♂</b></big></font>\t\t";
                ((TextView)rootView.findViewById(R.id.textSpeciesContent)).setText(habitat.getSpecies());
                ((TextView)rootView.findViewById(R.id.textBinNameContent)).setText(habitat.getBinName());
                ((TextView)rootView.findViewById(R.id.textConsStatusContent)).setText(habitat.getConsStatus());
                ((TextView)rootView.findViewById(R.id.textDietContent)).setText(habitat.getDiet());
                ((TextView)rootView.findViewById(R.id.textNatHabitatContent)).setText(habitat.getNatHabitat());
                ((TextView)rootView.findViewById(R.id.textNumOffspringContent)).setText(String.valueOf(habitat.getOffsprings()));
                ((TextView)rootView.findViewById(R.id.textAgeMaturityContent)).setText(getResources().getQuantityString(R.plurals.timeMonths, (int) Math.ceil(habitat.getMatAge()), String.valueOf(habitat.getMatAge())));
                ((TextView)rootView.findViewById(R.id.textGestPeriodContent)).setText(getResources().getQuantityString(R.plurals.timeMonths, habitat.getGestPeriod(), habitat.getGestPeriod()));
                ((TextView)rootView.findViewById(R.id.textSizeFContent)).setText(Html.fromHtml(femaleSymbol + getString(R.string.sizeMeters, String.valueOf(habitat.getF_size()))));
                ((TextView)rootView.findViewById(R.id.textSizeMContent)).setText(Html.fromHtml(maleSymbol + getString(R.string.sizeMeters, String.valueOf(habitat.getM_size()))));
                ((TextView)rootView.findViewById(R.id.textLifeExpFContent)).setText(Html.fromHtml(femaleSymbol + getResources().getQuantityString(R.plurals.timeYears, habitat.getF_lifeExpectancy(), habitat.getF_lifeExpectancy())));
                ((TextView)rootView.findViewById(R.id.textLifeExpMContent)).setText(Html.fromHtml(maleSymbol + getResources().getQuantityString(R.plurals.timeYears, habitat.getM_lifeExpectancy(), habitat.getM_lifeExpectancy())));
                ((TextView)rootView.findViewById(R.id.textWeightFContent)).setText(Html.fromHtml(femaleSymbol + getString(R.string.weightKilogram, String.valueOf(habitat.getF_weight()))));
                ((TextView)rootView.findViewById(R.id.textWeightMContent)).setText(Html.fromHtml(maleSymbol + getString(R.string.weightKilogram, String.valueOf(habitat.getM_weight()))));
            }
        });
        return rootView;
    }
}
