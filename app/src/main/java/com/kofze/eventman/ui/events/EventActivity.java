package com.kofze.eventman.ui.events;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kofze.eventman.R;
import com.kofze.eventman.adapters.EventPagerAdapter;
import com.kofze.eventman.datamodels.Event;

public class EventActivity extends AppCompatActivity {

    private ImageView eventDp;
    private TextView eventTitle;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private Event event;

    private TextView eventOwner;
    private ImageView ownerProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().hide();
        
        init();


        EventHomeFragment eventHomeFragment = new EventHomeFragment(event);
        GalleryFragment galleryFragment = new GalleryFragment(event);

        EventPagerAdapter eventPagerAdapter = new EventPagerAdapter(getApplicationContext(),this);

        eventPagerAdapter.addFragment(eventHomeFragment);
        eventPagerAdapter.addFragment(galleryFragment);

        viewPager.setAdapter(eventPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Home");
                    break;
                case 1:
                    tab.setText("Gallery");
                    break;
            }
        }).attach();
    }

    private void init() {
        //initialize views
        eventDp = findViewById(R.id.event_image);
        eventTitle = findViewById(R.id.event_title);
        tabLayout = findViewById(R.id.event_tabs);
        viewPager = findViewById(R.id.event_view_pager);

        eventOwner = findViewById(R.id.owner);
        ownerProfilePic = findViewById(R.id.ownerProfile);

        //get event from intent
        event = (Event) getIntent().getParcelableExtra("event");
        try {
            //set event data
            eventTitle.setText(event.getTitle());
            // Glide is used to load image from url
            Glide.with(this).load(event.getImage_url()).into(eventDp);
            eventOwner.setText(event.getOwner());
            Glide.with(getApplicationContext()).load(event.getOwnerProfileUrl()).into(ownerProfilePic);
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to fetch event data", Toast.LENGTH_SHORT).show();
        }


    }
}