package com.kofze.eventman.ui.events;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kofze.eventman.R;
import com.kofze.eventman.adapters.ImagesAdapter;
import com.kofze.eventman.datamodels.Event;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GalleryFragment} factory method to
 * create an instance of this fragment.
 */
public class GalleryFragment extends Fragment {

    private static final int PICK_IMAGES_REQUEST = 1001;
    private Event event;
    private Button addImagesButton;
    private RecyclerView galleryRecyclerView;
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<Uri> filePath = new ArrayList<>();

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

        addImagesButton = root.findViewById(R.id.add_images_button);
        galleryRecyclerView = root.findViewById(R.id.event_gallery_recycler);

        addImagesButton.setOnClickListener(v -> {
          Intent imagePicker = new Intent(Intent.ACTION_GET_CONTENT);
          imagePicker.setType("image/*");
          imagePicker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
          startActivityForResult(imagePicker, PICK_IMAGES_REQUEST);
        });



        ImagesAdapter adapter = new ImagesAdapter(getContext(),imageUrls);
        retrieveImages(adapter);

        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));


        galleryRecyclerView.setAdapter(adapter);



        return root;
    }

    private void retrieveImages(ImagesAdapter adapter){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        //get url of all images from a firebase storage
        storage.getReference().child("images/event/").child(event.getId()).listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageUrls.add(uri.toString());
                    adapter.notifyDataSetChanged();
                });
            }

        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_IMAGES_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        filePath.clear();
                        if (data.getClipData() != null) {
                            for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                                ClipData.Item item = data.getClipData().getItemAt(index);
                                filePath.add(item.getUri());
                            }
                        }
                        else {
                            filePath.add(data.getData());
                        }
                        
                        uploadImages();
                    }
                    else{
                        Toast.makeText(getContext(), "No File Picked", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private void uploadImages() {
        //upload images to firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        for (int index = 0; index < filePath.size(); index++) {
            Uri uri = filePath.get(index);
            StorageReference imageRef = storageRef.child("images/event/" + event.getId() + "/"+ user.getUid() + uri.getLastPathSegment());
            imageRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    imageUrls.add(uri1.toString());
                    galleryRecyclerView.getAdapter().notifyDataSetChanged();
                });
            });
        }
    }
}