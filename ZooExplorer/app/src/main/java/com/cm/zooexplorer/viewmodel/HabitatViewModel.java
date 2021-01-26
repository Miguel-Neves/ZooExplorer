package com.cm.zooexplorer.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cm.zooexplorer.models.Habitat;
import com.cm.zooexplorer.repository.Repository;

import java.util.List;

public class HabitatViewModel extends AndroidViewModel {
    MutableLiveData<List<Habitat>> habitatsLiveData;
    private Repository repository;

    public HabitatViewModel(Application application){
        super(application);

        repository = new Repository(application);
        habitatsLiveData = repository.getHabitats();
    }
    public MutableLiveData<List<Habitat>> getHabitatsLiveData() {
        return habitatsLiveData;
    }

    public MutableLiveData<Habitat> getHabitat(String id){
        return repository.getHabitat(id);
    }

    public MutableLiveData<List<String>> getHabitatPhotoPaths(String id){
        return repository.getHabitatPhotoPaths(id);
    }
}
