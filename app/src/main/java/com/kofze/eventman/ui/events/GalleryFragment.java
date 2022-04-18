package com.kofze.eventman.ui.events;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kofze.eventman.R;
import com.kofze.eventman.datamodels.Event;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {

    private Event event;

    public GalleryFragment() {
        // Required empty public constructor
    }

    public GalleryFragment(Event event) {
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
        View root =  inflater.inflate(R.layout.fragment_gallery, container, false);



        return root;
    }
}