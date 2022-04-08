package com.kofze.eventman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SignupActivity extends AppCompatActivity {

    private static final int REQ_PICKLOGO = 1001;
    private static final int STORAGE_REQUEST = 1002;
    private static final int RC_SIGN_IN = 1003;


    private ImageButton profile;
    private SignInButton googlesignInButton;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        profile = findViewById(R.id.ownerProfile);
        googlesignInButton = findViewById(R.id.google_sign_in);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .requestIdToken(getString(R.string.gToken))
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googlesignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin=googleSignInClient.getSignInIntent();
                startActivityForResult(signin,RC_SIGN_IN);
            }
        });
    }

    private void pickImage() {
        Intent logo_picker = new Intent(Intent.ACTION_GET_CONTENT);
        logo_picker.setType("image/*");
        logo_picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
//        startActivityForResult(logo_picker, REQ_PICKLOGO);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .setActivityTitle("Event Icon")
                .setActivityMenuIconColor(R.color.design_default_color_secondary)
                .setBackgroundColor(R.color.design_default_color_background)
                .setGuidelinesColor(R.color.design_default_color_background)
                .start(SignupActivity.this);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult activityResult=CropImage.getActivityResult(data);
            if (!(checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
            }
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                Uri croppedProfURI = activityResult.getUri();
                profile.setImageURI(croppedProfURI);

            }
            else{
                Toast.makeText(this, "Please grant Storage Permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
            }

        }

        if (requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);


                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
                firebaseAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        return;
                    }
                });

                String user_name = account.getDisplayName();
                String prof_url;

                try {
                    prof_url = account.getPhotoUrl().toString();
                }
                catch (Exception e)
                {
                    prof_url = "";
                }

                String Id = account.getId();
                Intent intent = new Intent();
                intent.putExtra("profile",prof_url);
                intent.setClass(getApplicationContext(),MainActivity.class);
                Toast.makeText(this, ""+Id+"\n"+user_name+"\n"+prof_url, Toast.LENGTH_SHORT).show();

                startActivity(intent);
                finish();

            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Toast.makeText(this, "Sign In Failed. Try Again! \n"+e.getLocalizedMessage() , Toast.LENGTH_SHORT).show();
            }
        }
    }
}