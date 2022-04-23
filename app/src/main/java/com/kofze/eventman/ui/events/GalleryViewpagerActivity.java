package com.kofze.eventman.ui.events;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager2.widget.ViewPager2;
import com.kofze.eventman.R;
import com.kofze.eventman.adapters.EventPagerAdapter;

import java.util.ArrayList;

public class GalleryViewpagerActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private ArrayList<String> images;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_viewpager);
        getSupportActionBar().hide();

        viewPager = findViewById(R.id.gallleryViewPager);
        images = getIntent().getStringArrayListExtra("images");
        position = getIntent().getIntExtra("position", 0);


        EventPagerAdapter adapter = new EventPagerAdapter(getApplicationContext(),this);

        for (String image : images) {
            adapter.addFragment(new ImageFragment(image));
        }

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);


    }
}