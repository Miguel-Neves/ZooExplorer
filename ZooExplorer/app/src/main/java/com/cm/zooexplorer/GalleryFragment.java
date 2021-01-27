package com.cm.zooexplorer;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cm.zooexplorer.adapters.GalleryAdapter;
import com.cm.zooexplorer.models.Habitat;
import com.cm.zooexplorer.viewmodel.HabitatViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static android.os.Environment.getExternalStoragePublicDirectory;


/**
 * A simple {@link Fragment} subclass.
 * Contains the gallery view, a grid of pictures of the associated habitat and button to take and upload a new one.
 */
public class GalleryFragment extends Fragment {

    private static final String TAG = "ZooExplorer-GALLERY";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3;
    private static GalleryFragment galleryFragment;
    private Uri currentPictureUri;
    private RecyclerView galleryRecyclerView;
    private GalleryAdapter adapter;
    private List<StorageReference> imgRefs = new LinkedList<>();
    private HabitatViewModel habitatViewModel;
    private String habitat_id;

    public GalleryFragment(String habitat_id) {
        this.habitat_id = habitat_id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        habitatViewModel = new ViewModelProvider(this).get(HabitatViewModel.class);
        habitatViewModel.getHabitatPhotoPaths(habitat_id).observe(this, new Observer<List<StorageReference>>() {
            @Override
            public void onChanged(List<StorageReference> storageReferences) {
                adapter.setImgRefs(storageReferences);
                //progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

        galleryRecyclerView = rootView.findViewById(R.id.gallery_recycler_view);
        adapter = new GalleryAdapter(imgRefs);
        galleryRecyclerView.setAdapter(adapter);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.galleryNumCol)));

        checkStoragePermissions();

        FloatingActionButton pictureButton = rootView.findViewById(R.id.faButtonAddPhoto);
        pictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    File picture = createImageFile();
                    currentPictureUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", picture);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPictureUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (IOException e) {
                    Toast.makeText(getContext(), "Error: Could not access the gallery directory.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Could not access the gallery directory. Error: ", e);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), "Error: Could not initiate the image capture.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Could not initiate the image capture. Error: ", e);
                }
            }
        });

        return rootView;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(new Date());
        String imageFileName = "Habitat_" + timeStamp;    // TODO add habitat id
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "ZooExplorer");
        if (!storageDir.exists() && !storageDir.mkdir())
            Log.e(TAG, "Could not create image storage directory \"" + storageDir.getAbsolutePath() + "\".");
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(currentPictureUri);
            getActivity().sendBroadcast(mediaScanIntent);
            Log.i(TAG, "Image saved in the device's gallery.");
            Toast.makeText(getContext(), "Image saved in the device's gallery.", Toast.LENGTH_LONG).show();

            habitatViewModel.uploadToFirebase(currentPictureUri, habitat_id);
        }
    }

    private void checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED)
                    checkStoragePermissions();
        }
    }


    public static GalleryFragment newInstance(String habitat_id) {
        return galleryFragment != null ? galleryFragment : new GalleryFragment(habitat_id);
    }
}
