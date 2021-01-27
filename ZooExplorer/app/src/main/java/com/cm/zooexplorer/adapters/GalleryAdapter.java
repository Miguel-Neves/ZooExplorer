package com.cm.zooexplorer.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cm.zooexplorer.R;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageViewHolder> {
    private List<StorageReference> imagePaths;
    private Context context;

    public GalleryAdapter(List<StorageReference> imagePaths){
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        return new ImageViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        StorageReference currentImgRef = imagePaths.get(position);
        Glide.with(context).load(currentImgRef).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public void setImgRefs(List<StorageReference> storageReferences) {
        this.imagePaths = storageReferences;
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        public final ImageView img;
        final GalleryAdapter adapter;

        public ImageViewHolder(@NonNull View itemView, GalleryAdapter adapter) {
            super(itemView);

            img = itemView.findViewById(R.id.gallery_img);
            this.adapter = adapter;
        }
    }
}
