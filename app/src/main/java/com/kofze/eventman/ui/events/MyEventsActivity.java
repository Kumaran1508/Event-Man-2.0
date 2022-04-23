package com.kofze.eventman.ui.events;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.kofze.eventman.R;
import com.kofze.eventman.adapters.DashboardEventsAdapter;
import com.kofze.eventman.dao.EventsRepo;
import com.kofze.eventman.datamodels.Event;

import java.util.ArrayList;

public class MyEventsActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        getSupportActionBar().hide();

        initializeViews();

        loadEvents();


    }

    private void loadEvents() {
        EventsRepo eventsRepo = EventsRepo.getInstance();
        ArrayList<Event> events = new ArrayList<>();


        DashboardEventsAdapter dashboardEventsAdapter = new DashboardEventsAdapter(this,events);
        eventsRecyclerView.setHasFixedSize(true);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        eventsRecyclerView.setAdapter(dashboardEventsAdapter);

        eventsRepo.getEventsbyUser(events,dashboardEventsAdapter, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void initializeViews() {
        eventsRecyclerView = findViewById(R.id.events_list);
    }
}