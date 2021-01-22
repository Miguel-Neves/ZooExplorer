package com.cm.zooexplorer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
*/
import java.util.LinkedList;
import java.util.List;

import com.cm.zooexplorer.adapters.HabitatsAdapter;
import com.cm.zooexplorer.models.Habitat;
import com.cm.zooexplorer.models.Location;

import com.cm.zooexplorer.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HabitatsFragment extends Fragment {
    private final List<Habitat> habitats = new LinkedList<>();
    private RecyclerView habitatsRecyclerView;
    private HabitatsAdapter adapter;
    //private CollectionReference firestoreRef = FirebaseFirestore.getInstance().collection("habitats");

    public HabitatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //createHabitats();

        habitats.add(new Habitat("1", "Panthera Leo", "", "", 0,
        0.0, 0.0, 0, new Location(0.0,0.0),
        0, 0.0, 0.0, 0.0,
        "", 0, "Panthera Leo", "lion", "lion"));
        habitats.add(new Habitat("2", "Giraffe", "", "", 0,
                0.0, 0.0, 0, new Location(0.0,0.0),
                0, 0.0, 0.0, 0.0,
                "", 0, "Giraffe", "giraffe", "giraffe"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_habitats, container, false);

        habitatsRecyclerView = rootView.findViewById(R.id.habitats_recycler_view);
        adapter = new HabitatsAdapter(habitats);
        habitatsRecyclerView.setAdapter(adapter);
        habitatsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    /*private void createHabitats(){
        firestoreRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if(task.isSuccessful())
                    for(QueryDocumentSnapshot doc : task.getResult())
                        Log.i("habitats", doc.getId() + "=>" + doc.getData());
            }
        });
    }*/

    public static HabitatsFragment newInstance(){
        return new HabitatsFragment();
    }
}
