package com.cm.zooexplorer.repository;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.cm.zooexplorer.models.Habitat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Repository {
    private Application application;
    private MutableLiveData<List<Habitat>> habitatsMutableLiveData;
    private MutableLiveData<Habitat> habitat;
    private MutableLiveData<List<StorageReference>> photoPaths;
    private CollectionReference firestoreRef = FirebaseFirestore.getInstance().collection("habitats");
    private FirebaseStorage storageRef = FirebaseStorage.getInstance("gs://cm-zoo-explorer.appspot.com");

    public Repository(Application application){
        this.application = application;
        habitatsMutableLiveData = new MutableLiveData<>();
        habitat = new MutableLiveData<>();
        photoPaths = new MutableLiveData<>();
    }

    public MutableLiveData<List<Habitat>> getHabitats(){

        firestoreRef.addSnapshotListener(application.getMainExecutor(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
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

    public MutableLiveData<Habitat> getHabitat(String id){

        firestoreRef.document(id).addSnapshotListener(application.getMainExecutor(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot doc, @Nullable FirebaseFirestoreException error) {
                Habitat h = doc.toObject(Habitat.class);
                h.setId(doc.getId());
                habitat.postValue(h);
            }
        });
        return habitat;
    }

    public MutableLiveData<List<StorageReference>> getHabitatPhotoPaths(String id){
        storageRef.getReference().child("photos").child(id).listAll().addOnSuccessListener(application.getMainExecutor(), new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                /*List<String> paths = new LinkedList<>();

                for(StorageReference sr : listResult.getItems()){
                 paths.add(sr.getPath());
                }
                photoPaths.postValue(paths);*/
                photoPaths.postValue(listResult.getItems());
            }
        });
        return photoPaths;
    }

    public void uploadToFirebase(Uri uri, final String habitat_id) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());

        StorageReference ref = storageRef.getReference().child("photos").child(habitat_id).child(timeStamp + ".jpg");
        Log.i("Referencia", ref.getPath());
        ref.putFile(uri).addOnSuccessListener(application.getMainExecutor(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("ISTO", "-----> funcionou");
                getHabitatPhotoPaths(habitat_id);
            }
        }).addOnFailureListener(application.getMainExecutor(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("ISTO", "----->  NAO funcionou");
            }
        });
    }
}

/*  // get storage reference
  Reference get storageReference{
    return storage.ref();
  }

  // get the list of photos of an habitat
  Future<List<String>> photos(String id){
    final Reference storRef = storage.ref().child('photos').child(id);
    return storRef.listAll().then((ListResult list){
        return list.items.map((Reference ref){
            return ref.fullPath;
          }
        ).toList();
      }
    );
  }
}*/