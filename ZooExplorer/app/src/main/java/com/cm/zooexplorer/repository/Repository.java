package com.cm.zooexplorer.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.cm.zooexplorer.models.Habitat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class Repository {
    private Application application;
    MutableLiveData<List<Habitat>> habitatsMutableLiveData;
    private CollectionReference firestoreRef = FirebaseFirestore.getInstance().collection("habitats");

    public Repository(Application application){
        this.application = application;
        habitatsMutableLiveData = new MutableLiveData<>();
    }


    public MutableLiveData<List<Habitat>> getHabitats(){

        firestoreRef.get().addOnSuccessListener(application.getMainExecutor(), new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                List<Habitat> habitats = new LinkedList<>();

                for(DocumentSnapshot doc : querySnapshot.getDocuments()){
                    Habitat habitat = doc.toObject(Habitat.class);
                    habitat.setId(doc.getId());
                    habitats.add(habitat);
                }
                habitatsMutableLiveData.postValue(habitats);
            }
        });

        return habitatsMutableLiveData;
    }
/*
    public MutableLiveData<List<Habitat>> getHabitatsMutableLiveData() {
        return habitatsMutableLiveData;
    }

 */
}
