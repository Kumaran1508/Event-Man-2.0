package com.kofze.eventman.ui.home;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kofze.eventman.R;
import com.kofze.eventman.ui.auth.ui.login.LoginActivity;

public class HomeFragment extends Fragment {

    private ImageView profile;
    private Button logout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        profile = root.findViewById(R.id.profile);
        logout = root.findViewById(R.id.logout);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        String profURL = firebaseUser.getPhotoUrl().toString();

        if (profURL!=""){
            Glide.with(this).load(profURL).into(profile);
        }


        return root;
    }
}