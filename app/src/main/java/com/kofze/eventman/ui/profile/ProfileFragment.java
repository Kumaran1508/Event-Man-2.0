package com.kofze.eventman.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kofze.eventman.R;
import com.kofze.eventman.ui.SettingsActivity;
import com.kofze.eventman.ui.auth.LoginActivity;
import com.kofze.eventman.ui.events.CreateEventActivity;
import com.kofze.eventman.ui.events.MyEventsActivity;


public class ProfileFragment extends Fragment {

    private ImageView profile;
    private TextView username;
    private NavigationView profileMenu;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profile = root.findViewById(R.id.profile);
        username = root.findViewById(R.id.user_name);
        profileMenu = root.findViewById(R.id.profile_menu);

        profileMenu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_edit_profile:
                    startActivity(new Intent(getActivity(), EditProfileActivity.class));
                    break;
                case R.id.nav_create_event:
                    startActivity(new Intent(getActivity(), CreateEventActivity.class));
                    break;
                case R.id.nav_my_events:
                    startActivity(new Intent(getActivity(), MyEventsActivity.class));
                    break;
                case R.id.nav_settings:
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    break;
                case R.id.nav_logout:
                    logout();

            }
            return true;
        });

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        username.setText(firebaseUser.getDisplayName());

        try{
            String profURL = firebaseUser.getPhotoUrl().toString();
            if (profURL!="")  Glide.with(this).load(profURL).into(profile);
        }catch (Exception e){

        }

        return root;
    }

    private void logout(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signOut();
        Intent intent = new Intent();
        intent.setClass(getContext(), LoginActivity.class);
        intent.putExtra("autoLogin",false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestIdToken(getString(R.string.gToken))
                .requestProfile()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        googleSignInClient.signOut();


        startActivity(intent);
        getActivity().finish();
    }
}