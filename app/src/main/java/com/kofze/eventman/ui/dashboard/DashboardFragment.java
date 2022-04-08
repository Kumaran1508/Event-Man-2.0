package com.kofze.eventman.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kofze.eventman.R;
import com.kofze.eventman.adapters.DashboardEventsAdapter;
import com.kofze.eventman.dao.EventsRepo;
import com.kofze.eventman.datamodels.Event;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        SearchView searchView = root.findViewById(R.id.events_search);
        RecyclerView eventsRecycler = root.findViewById(R.id.events_list);

        EventsRepo eventsRepo = EventsRepo.getInstance();
        ArrayList<Event> events = new ArrayList<>();


        DashboardEventsAdapter dashboardEventsAdapter = new DashboardEventsAdapter(getContext(),events);
        eventsRecycler.setHasFixedSize(true);
        eventsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsRecycler.setAdapter(dashboardEventsAdapter);

        eventsRepo.getAllEvents(events,dashboardEventsAdapter);

        return root;
    }
}