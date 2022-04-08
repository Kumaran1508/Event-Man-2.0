package com.kofze.eventman.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kofze.eventman.R;
import com.kofze.eventman.datamodels.Event;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class DashboardEventsAdapter extends RecyclerView.Adapter<DashboardEventsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Event> events;

    public DashboardEventsAdapter(Context context, ArrayList<Event> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.event_item,parent,false);
        ViewHolder eventsViewholder = new ViewHolder(itemView);
        return eventsViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        return;
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView eventTitle;
        private TextView eventOwner;
        private TextView start_time;
        private ImageView eventCover;
        private ImageView ownerProfile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventOwner = itemView.findViewById(R.id.owner);
            start_time = itemView.findViewById(R.id.time);
            eventCover = itemView.findViewById(R.id.eventCover);
            ownerProfile = itemView.findViewById(R.id.ownerProfile);

        }
    }
}
