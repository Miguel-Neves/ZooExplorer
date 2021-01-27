package com.cm.zooexplorer.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Set;

import com.cm.zooexplorer.HabitatProfileActivity;
import com.cm.zooexplorer.HabitatsFragment;
import com.cm.zooexplorer.QrCodeActivity;
import com.cm.zooexplorer.R;
import com.cm.zooexplorer.models.Habitat;

import static android.content.Context.MODE_PRIVATE;

public class HabitatsAdapter extends RecyclerView.Adapter<HabitatsAdapter.HabitatViewHolder> {
    //private static final String PREFERENCES_NAME = "com.cm.zooexplorer.UNLOCKED_HABITATS";
    private List<Habitat> habitats;
    private Context context;
    public static String HABITAT_IDENTIFIER = "com.cm.zooexplorer.HABITAT_IDENTIFIER";

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
        final Habitat currentHabitat = habitats.get(position);

        String habitatName = "Habitat " + currentHabitat.getId();
        holder.habitatName.setText(habitatName);

        holder.backgroundImg.setImageResource(getImage(currentHabitat.getImageName()));
        ColorMatrix matrix = new ColorMatrix();
        ColorMatrixColorFilter filter;

        SharedPreferences prefs = context.getSharedPreferences(HabitatsFragment.PREFERENCES_NAME, MODE_PRIVATE);
        Set<String> unlockedHabitats = prefs.getStringSet(HabitatsFragment.UNLOCKED_HABITATS, null);

        if(unlockedHabitats!=null && unlockedHabitats.contains(currentHabitat.getId())){
            holder.habitatAnimal.setText(currentHabitat.getSpecies());
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HabitatProfileActivity.class);
                    intent.putExtra(HABITAT_IDENTIFIER, currentHabitat.getId());
                    context.startActivity(intent);
                }
            });

            matrix.setSaturation(1);
            filter = new ColorMatrixColorFilter(matrix);
            holder.backgroundImg.setColorFilter(filter);
        }else{
            holder.habitatAnimal.setText(R.string.locked_text);

            matrix.setSaturation(0);
            filter = new ColorMatrixColorFilter(matrix);
            holder.backgroundImg.setColorFilter(filter);
        }
    }

    public void setHabitats(List<Habitat> habitats){
        this.habitats = habitats;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return habitats.size();
    }

    private int getImage(String imgName){
        return context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
    }

    class HabitatViewHolder extends RecyclerView.ViewHolder {
        final TextView habitatName, habitatAnimal;
        final ImageView backgroundImg;
        final CardView cardView;
        final HabitatsAdapter adapter;

        public HabitatViewHolder(View itemView, HabitatsAdapter adapter) {
            super(itemView);
            habitatName = itemView.findViewById(R.id.habitat_name);
            habitatAnimal = itemView.findViewById(R.id.habitat_animal);
            backgroundImg = itemView.findViewById(R.id.background_img);
            cardView = itemView.findViewById(R.id.card_view);
            this.adapter = adapter;
        }
    }
}
