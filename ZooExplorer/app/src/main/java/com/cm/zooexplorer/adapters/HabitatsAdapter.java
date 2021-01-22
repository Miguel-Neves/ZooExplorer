package com.cm.zooexplorer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.cm.zooexplorer.R;
import com.cm.zooexplorer.models.Habitat;

public class HabitatsAdapter extends RecyclerView.Adapter<HabitatsAdapter.HabitatViewHolder> {
    private final List<Habitat> habitats;
    private Context context;

    public HabitatsAdapter(List<Habitat> habitats) {
        this.habitats = habitats;
    }

    @NonNull
    @Override
    public HabitatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.habitats_item,
                                                                         parent, false);
        return new HabitatViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitatViewHolder holder, int position) {
        Habitat currentHabitat = habitats.get(position);
        holder.habitatAnimal.setText(currentHabitat.getSpecies());

        String habitatName = "Habitat " + currentHabitat.getId();
        holder.habitatName.setText(habitatName);
        // TODO: change the image background
        holder.backgroundImg.setImageResource(getImage(currentHabitat.getImageName()));
    }

    @Override
    public int getItemCount() {
        return habitats.size();
    }

    private int getImage(String imgName){
        return context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
    }

    class HabitatViewHolder extends RecyclerView.ViewHolder {
        public final TextView habitatName, habitatAnimal;
        public final ImageView backgroundImg;
        final HabitatsAdapter adapter;

        public HabitatViewHolder(View itemView, HabitatsAdapter adapter) {
            super(itemView);
            habitatName = itemView.findViewById(R.id.habitat_name);
            habitatAnimal = itemView.findViewById(R.id.habitat_animal);
            backgroundImg = itemView.findViewById(R.id.background_img);
            this.adapter = adapter;
        }
    }
}
