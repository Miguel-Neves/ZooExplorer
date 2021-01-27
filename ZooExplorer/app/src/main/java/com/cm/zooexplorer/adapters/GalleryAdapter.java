package com.cm.zooexplorer.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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
    private static String ZOOM_IMAGE = "com.cm.zooexplorer.ZOOM_IMAGE";
    private List<StorageReference> imagePaths;
    private Context context;
    private Dialog dialog;

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
    public void onBindViewHolder(@NonNull final ImageViewHolder holder, int position) {
        dialog = new Dialog(context);
        final StorageReference currentImgRef = imagePaths.get(position);
        Glide.with(context).load(currentImgRef).into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.popup_image);
                ImageView img = dialog.findViewById(R.id.zoom_img);
                img.setImageBitmap(((BitmapDrawable)holder.img.getDrawable()).getBitmap());
                dialog.show();
            }
        });
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
