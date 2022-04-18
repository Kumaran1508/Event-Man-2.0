package com.kofze.eventman.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.kofze.eventman.R;
import com.kofze.eventman.datamodels.Event;
import com.kofze.eventman.ui.events.EventActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

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
        Event event = events.get(position);
        holder.eventTitle.setText(event.getTitle());

        //Time Formatter
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        Date date = event.getStart_time().toDate();
        String dateString = format.format(date);
        holder.start_time.setText(dateString);
        holder.eventOwner.setText(event.getOwner());
        Glide.with(context).load(event.getImage_url()).placeholder(R.drawable.loading).centerCrop().into(holder.eventCover);
        try {
            Glide.with(context).load(event.getOwnerProfileUrl()).placeholder(R.drawable.loading).circleCrop().into(holder.ownerProfile);
        }
        catch (Exception e){
            Toast.makeText(context, "Unable to load owner profile", Toast.LENGTH_SHORT).show();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // put Event from position of Events in an intent and send to event activity
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("event",event );
                context.startActivity(intent);
            }
        });

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
