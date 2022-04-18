package com.kofze.eventman.ui.events;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kofze.eventman.FocusedMapView;
import com.kofze.eventman.R;
import com.kofze.eventman.datamodels.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventHomeFragment} factory method to
 * create an instance of this fragment.
 */
public class EventHomeFragment extends Fragment {

    private Event event;
    private TextView eventOwner;
    private TextView startTime;
    private TextView endTime;
    private TextView eventMode;
    private TextView eventCategory;
    private TextView eventVisibility;
    private TextView eventDescription;
    private ImageView ownerProfilePic;
    private FocusedMapView mapView;
    private Button inviteButton;


    public EventHomeFragment() {
        // Required empty public constructor
    }

    public EventHomeFragment(Event event) {
        this.event = event;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_event_home, container, false);

        eventOwner = root.findViewById(R.id.owner);
        startTime = root.findViewById(R.id.startTime);
        endTime = root.findViewById(R.id.endTime);
        eventMode = root.findViewById(R.id.eventMode);
        eventCategory = root.findViewById(R.id.eventCategory);
        eventVisibility = root.findViewById(R.id.eventVisibility);
        eventDescription = root.findViewById(R.id.event_description);
        ownerProfilePic = root.findViewById(R.id.ownerProfile);
        mapView = root.findViewById(R.id.event_location);
        inviteButton = root.findViewById(R.id.invite_button);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng eventLocation = new LatLng(event.getLocation().getLatitude(),event.getLocation().getLongitude());
                final MarkerOptions markerOptions = new MarkerOptions().position(eventLocation).draggable(true);
                final Marker marker=googleMap.addMarker(markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 13));

                googleMap.getUiSettings().setZoomGesturesEnabled(true);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        float lat=Float.parseFloat(eventLocation.latitude+"");
                        float lng=Float.parseFloat(eventLocation.longitude+"");
                        String url=String.format(Locale.ENGLISH,"http://www.google.com/maps/dir/?api=1&destination=%f,%f",lat,lng);
                        Intent itnt=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        itnt.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                        startActivity(itnt);
                    }
                });
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();

        eventOwner.setText(event.getOwner());
        eventMode.setText(event.getEventMode().toString());
        eventCategory.setText(event.getEventCategory().toString());
        eventVisibility.setText(event.isPublic() ? "Public" : "Private");
        eventDescription.setText(event.getDescription());

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Date date = event.getStart_time().toDate();
        String dateString = format.format(date);
        startTime.setText(dateString);

        date = event.getEnd_time().toDate();
        dateString = format.format(date);
        endTime.setText(dateString);

        //glide to load the profile picture
        Glide.with(getContext()).load(event.getOwnerProfileUrl()).into(ownerProfilePic);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}