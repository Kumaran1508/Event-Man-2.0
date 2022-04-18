package com.kofze.eventman.ui.events;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kofze.eventman.FocusedMapView;
import com.kofze.eventman.R;
import com.kofze.eventman.datamodels.Event;
import com.kofze.eventman.datamodels.EventCategory;
import com.kofze.eventman.datamodels.EventMode;
import com.kofze.eventman.datamodels.EventVisibility;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class CreateEventActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final int STORAGE_REQUEST = 1001;
    private MaterialAutoCompleteTextView eventCategory,eventMode,eventVisibility,eventStartTime,eventEndTime;
    private TextInputLayout eventName,eventDescription;
    private long date_time,date_time_end;
    private FocusedMapView mapView;
    private Button createButton;
    private LatLng eventLocation;
    private GoogleMap googleMap;
    private Timestamp starttime;
    private Timestamp endtime;
    private TextInputLayout eventCategoryBox,eventModeBox,eventVisibilityBox,eventStartTimeBox,eventEndTimeBox;
    private ImageButton imageButton;
    private Uri eventDpUri;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initializeViews();

        /**
         *      Set dropdown options for form
         */

        List eventCategories = Arrays.asList(EventCategory.values());
        ArrayAdapter catergoryAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,eventCategories);
        eventCategory.setAdapter(catergoryAdapter);

        List eventModes = Arrays.asList(EventMode.values());
        ArrayAdapter eventModeAdaper = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,eventModes);
        eventMode.setAdapter(eventModeAdaper);

        List eventVisibilities = Arrays.asList(EventVisibility.values());
        ArrayAdapter visibilityAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1,eventVisibilities);
        eventVisibility.setAdapter(visibilityAdapter);

        //initialize map
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

    }



    private void setValidators() {

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(16,9);
            }
        });

        eventName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = eventName.getEditText().getText().toString();
                if (text.trim().length() == 0)
                    eventName.setError("Title cannot be empty");
                else if (text.trim().length() < 3)
                    eventName.setError("Event Title is too short");
                else
                    eventName.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        eventCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (eventCategory.getText()==null)
                    eventCategoryBox.setError("Choose Event Category");
                else
                    eventCategoryBox.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        eventMode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (eventMode.getText()==null)
                    eventModeBox.setError("Choose Event Mode");
                else
                    eventModeBox.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        eventVisibility.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (eventVisibility.getText()==null)
                    eventVisibilityBox.setError("Choose Event Visibility");
                else
                    eventVisibilityBox.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        eventStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Start Date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

                    datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @Override
                        public void onPositiveButtonClick(Long selection) {
                            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                    date_time = selection + (hourOfDay-5)*60*60*1000 + (minute-30)*60*1000;
                                    Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                                    utc.setTimeInMillis(date_time);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                    String formatted = format.format(utc.getTime());
                                    starttime = new Timestamp(date_time/1000,0);
                                    Toast.makeText(CreateEventActivity.this, ""+ starttime.toString(), Toast.LENGTH_SHORT).show();
                                    eventStartTime.setText(formatted);
                                    eventStartTimeBox.setErrorEnabled(false);
                                }
                            },false);
                            timePickerDialog.setThemeDark(false);
                            //timePickerDialog.showYearPickerFirst(false);
                            timePickerDialog.setTitle("Pick Start time");
                            timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
                        }
                    });

                    datePicker.show(getSupportFragmentManager(),"nothing");
                }
            }
        });

        eventEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Start Date").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

                    datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @Override
                        public void onPositiveButtonClick(Long selection) {
                            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                                    date_time = selection + (hourOfDay-5)*60*60*1000 + (minute-30)*60*1000;
                                    Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                                    utc.setTimeInMillis(date_time);
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                                    String formatted = format.format(utc.getTime());
                                    endtime = new Timestamp(date_time/1000,0);
                                    Toast.makeText(CreateEventActivity.this, ""+ starttime.toString(), Toast.LENGTH_SHORT).show();
                                    eventEndTime.setText(formatted);
                                    eventEndTimeBox.setErrorEnabled(false);
                                }
                            },false);
                            timePickerDialog.setThemeDark(false);
                            //timePickerDialog.showYearPickerFirst(false);
                            timePickerDialog.setTitle("Pick End time");
                            timePickerDialog.show(getFragmentManager(), "TimePickerDialog");
                        }
                    });

                    datePicker.show(getSupportFragmentManager(),"nothing");
                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check event name is empty
                if(eventName.getEditText().getText().toString().isEmpty()){
                    eventName.setError("Event name cannot be empty");
                    eventName.getEditText().requestFocus();
                }
                //else if event category is empty
                else if(eventCategory.getText().toString().isEmpty()){
                    eventCategoryBox.setError("Choose Event category");
                    eventCategory.requestFocus();
                }
                //else if event mode is empty
                else if(eventMode.getText().toString().isEmpty()){
                    eventModeBox.setError("Choose Event mode");
                    eventMode.requestFocus();
                }
                //else if event visibility is empty
                else if(eventVisibility.getText().toString().isEmpty()){
                    eventVisibilityBox.setError("Choose Event visibility");
                    eventVisibility.requestFocus();
                }
                //else if event start time is empty
                else if(eventStartTime.getText().toString().isEmpty()){
                    eventStartTimeBox.setError("Choose Event start time");
                    eventStartTime.requestFocus();
                }
                //else if event end time is empty
                else if(eventEndTime.getText().toString().isEmpty()){
                    eventEndTimeBox.setError("Choose Event end time");
                    eventEndTime.requestFocus();
                }
                //else if event location is empty
                else if(eventLocation == null){
                    Toast.makeText(CreateEventActivity.this, "Choose Event location", Toast.LENGTH_SHORT).show();
                    mapView.requestFocus();
                }
                else{
                    //create event
                    Event event = new Event();
                    event.setTitle(eventName.getEditText().getText().toString());
                    event.setEventCategory(EventCategory.valueOf(eventCategory.getText().toString()));
                    event.setEventMode(EventMode.valueOf(eventMode.getText().toString()));
                    event.setPublic(EventVisibility.valueOf(eventVisibility.getText().toString())==EventVisibility.PUBLIC);
                    event.setStart_time(starttime);
                    event.setEnd_time(endtime);
                    event.setLocation(new GeoPoint(eventLocation.latitude,eventLocation.longitude));
                    event.setDescription(eventDescription.getEditText().getText().toString());
                    event.setOwner(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    event.setOwnerProfileUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());

                    //upload EventDp to Firebase


                    //add event to Firestore
                    event.setId(FirebaseFirestore.getInstance().collection("events").document().getId());

                    if(eventDpUri != null){
                        uploadImage(event);
                    }



                }
            }
        });

    }

    //create event function
    private void createEvent(Event event){
        FirebaseFirestore.getInstance().collection("events").document(event.getId()).set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CreateEventActivity.this, "Event created", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateEventActivity.this, "Event creation failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickImage(int ratioX,int ratioY){
        Intent logo_picker = new Intent(Intent.ACTION_GET_CONTENT);
        logo_picker.setType("image/*");
        logo_picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
//        startActivityForResult(logo_picker, REQ_PICKLOGO);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(ratioX,ratioY)
                .setActivityTitle("Event Icon")
                .setActivityMenuIconColor(com.kofze.eventman.R.color.design_default_color_secondary)
                .setBackgroundColor(com.kofze.eventman.R.color.design_default_color_background)
                .setGuidelinesColor(R.color.design_default_color_background)
                .start(CreateEventActivity.this);
    }

    private void initializeViews() {
        eventCategory = findViewById(R.id.event_category);
        eventMode = findViewById(R.id.event_mode);
        eventVisibility = findViewById(R.id.event_visibility);

        eventName = findViewById(R.id.eventnamebox);
        eventStartTime = findViewById(R.id.event_date);
        eventEndTime = findViewById(R.id.event_end_date);
        eventDescription = findViewById(R.id.event_description_box);

        mapView = findViewById(R.id.event_location);
        createButton = findViewById(R.id.create_event);

        eventCategoryBox = findViewById(R.id.eventcategorybox);
        eventModeBox = findViewById(R.id.eventmodebox);
        eventVisibilityBox = findViewById(R.id.eventvisibilitybox);
        eventStartTimeBox = findViewById(R.id.eventstarttimebox);
        eventEndTimeBox = findViewById(R.id.eventendtimebox);

        imageButton = findViewById(R.id.event_dp);
    }

    private void uploadImage(Event event) {
        if (eventDpUri != null) {

            final StorageReference ref = firebaseStorage.getReference().child("images/events/" + event.getId());
            ref.putFile(eventDpUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // get the event image url
                                    String imageUrl = uri.toString();
                                    event.setImage_url(imageUrl);
                                    createEvent(event);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        this.googleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // request location permission
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        eventLocation = new LatLng(location.getLatitude(), location.getLongitude());

        final MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(0,0)).draggable(true);
        final Marker marker=googleMap.addMarker(markerOptions);

        if (location != null)
        {
            markerOptions.position(eventLocation);
            marker.setPosition(eventLocation);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }


        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                markerOptions.position(latLng);
                marker.setPosition(latLng);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

        setValidators();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    onMapReady(googleMap);
                }
            }
            else {
                Toast.makeText(this, "Please provide GPS permission to continue", Toast.LENGTH_LONG).show();
            }
        }
    }

    //onactivity result
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult activityResult=CropImage.getActivityResult(data);
            if (!(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
            }
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                eventDpUri = activityResult.getUri();
                imageButton.setImageURI(eventDpUri);

            }
            else{
                Toast.makeText(this, "Please grant Storage Permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
            }

        }
    }
}