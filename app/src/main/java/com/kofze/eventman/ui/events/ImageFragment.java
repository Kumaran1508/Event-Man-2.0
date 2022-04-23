package com.kofze.eventman.ui.events;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.kofze.eventman.R;


public class ImageFragment extends Fragment {

    String imageUrl;

    public ImageFragment() {
        // Required empty public constructor
    }

    public ImageFragment(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_image, container, false);

        PhotoView photoView = (PhotoView) root.findViewById(R.id.photo_view);
        Glide.with(this).load(imageUrl).into(photoView);

        return root;
    }
}