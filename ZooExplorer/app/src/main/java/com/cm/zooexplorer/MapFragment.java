package com.cm.zooexplorer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cm.zooexplorer.R;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


/**
 * A simple {@link Fragment} subclass.
 * Contains the map view, including the device's and habitat's locations.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "ZooExplorer-MAP";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static MapFragment mapFragment;
    private GoogleMap gmap;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supMapFrag = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        supMapFrag.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        // Load custom map style
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
            if (!success)
                Log.e(TAG, "Style parsing failed.");
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Could not find style. Error: ", e);
        }
        // Set initial camera position/zoom
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.63191945636097, -8.657524065137872), 17f));
        // Set map and zoom limits, to focus the view on the zoo experience
        googleMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(40.622055, -8.664741), new LatLng(40.637429, -8.653219)));
        googleMap.setMaxZoomPreference(18.5f);
        googleMap.setMinZoomPreference(15f);
        // Disable unnecessary map toolbar
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        // Enable user localization
        enableLocation();
        //googleMap.setMyLocationEnabled(true);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.633450, -8.657985))
                .title("Habitat 1\tⓘ").snippet("Lions")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.animal_marker_icon)))
                .setTag(1);
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.632134740413925, -8.658584348230372))
                .title("Habitat 2\tⓘ").snippet("Giraffes")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.animal_marker_icon))
                .alpha(0.7f))
                .setTag(2);
        // Set habitat page shortcut on marker info window tap
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i("DEBUG", "loading habitat " + marker.getTag() + " info page");
            }
        });
    }

    private void enableLocation() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (gmap != null) {
                gmap.setMyLocationEnabled(true);
                gmap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    enableLocation();
        }
    }

    public static MapFragment newInstance() {
        return mapFragment != null ? mapFragment : new MapFragment();
    }
}
