package com.kofze.eventman.dao;


import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.kofze.eventman.adapters.DashboardEventsAdapter;
import com.kofze.eventman.datamodels.Event;
import com.kofze.eventman.datamodels.EventCategory;

import java.util.ArrayList;
import java.util.List;

public class EventsRepo {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static EventsRepo eventsRepo;

    private EventsRepo(){
    }

    public static EventsRepo getInstance(){
        if (eventsRepo ==null)
            eventsRepo = new EventsRepo();
        return eventsRepo;
    }

    public void getAllEvents(List<Event> events, DashboardEventsAdapter adapter){
        firestore.collection("events").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                events.clear();
                for (DocumentSnapshot document : queryDocumentSnapshots){
                    Event event = document.toObject(Event.class);
                    event.setId(document.getId());
                    events.add(event);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    public void getEventsbyCategory(List<Event> events, DashboardEventsAdapter adapter, EventCategory category){
        firestore.collection("events").whereEqualTo("eventCategory",category.toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                events.clear();
                for (DocumentSnapshot document : queryDocumentSnapshots){
                    Event event = document.toObject(Event.class);
                    event.setId(document.getId());
                    events.add(event);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    // get events created by user with userId
    public void getEventsbyUser(List<Event> events, DashboardEventsAdapter adapter, String userId){
        firestore.collection("events").whereEqualTo("ownerId",userId).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                events.clear();
                for (DocumentSnapshot document : queryDocumentSnapshots){
                    Event event = document.toObject(Event.class);
                    event.setId(document.getId());
                    events.add(event);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

}
