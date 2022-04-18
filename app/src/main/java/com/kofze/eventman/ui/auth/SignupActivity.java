package com.kofze.eventman.ui.auth;

import android.text.TextUtils;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kofze.eventman.MainActivity;
import com.kofze.eventman.R;
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
    private TextInputLayout nameBox , emailBox , passwordBox , confirmPasswordBox;
    private Uri logoUri;
    private Button signupButton;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kofze.eventman.R.layout.activity_signup);
        getSupportActionBar().hide();

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        profile = findViewById(com.kofze.eventman.R.id.ownerProfile);
        googlesignInButton = findViewById(com.kofze.eventman.R.id.google_sign_in);

        // initialize TextInputLayout
        nameBox = findViewById(com.kofze.eventman.R.id.namebox);
        emailBox = findViewById(com.kofze.eventman.R.id.emailbox);
        passwordBox = findViewById(com.kofze.eventman.R.id.passwordbox);
        confirmPasswordBox = findViewById(com.kofze.eventman.R.id.confirmpasswordbox);

        // initialize Button
        signupButton = findViewById(com.kofze.eventman.R.id.signup);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage(1,1);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .requestIdToken(getString(com.kofze.eventman.R.string.gToken))
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googlesignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signin=googleSignInClient.getSignInIntent();
                startActivityForResult(signin,RC_SIGN_IN);
            }
        });

        // set error if text is empty
        nameBox.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(nameBox.getEditText().getText().toString().isEmpty()){
                        nameBox.setError("Name is required");
                    }
                }
            }
        });

        emailBox.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(emailBox.getEditText().getText().toString().isEmpty()){
                        emailBox.setError("Email is required");
                    }
                }
            }
        });

        passwordBox.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(passwordBox.getEditText().getText().toString().isEmpty()){
                        passwordBox.setError("Password is required");
                    }
                }
            }
        });

        confirmPasswordBox.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(confirmPasswordBox.getEditText().getText().toString().isEmpty()){
                        confirmPasswordBox.setError("Confirm Password is required");
                    }
                    if (!confirmPasswordBox.getEditText().getText().toString().equals(passwordBox.getEditText().getText().toString())){
                        confirmPasswordBox.setError("Password does not match");
                    }
                }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sign up
                if(!nameBox.getEditText().getText().toString().isEmpty() && !emailBox.getEditText().getText().toString().isEmpty() && !passwordBox.getEditText().getText().toString().isEmpty() && !confirmPasswordBox.getEditText().getText().toString().isEmpty()){
                    if (passwordBox.getEditText().getText().toString().equals(confirmPasswordBox.getEditText().getText().toString())){
                        signUp();
                    }
                }
            }
        });


    }

    private void pickImage(int ratioX,int ratioY){
        Intent logo_picker = new Intent(Intent.ACTION_GET_CONTENT);
        logo_picker.setType("image/*");
        logo_picker.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
//        startActivityForResult(logo_picker, REQ_PICKLOGO);
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(ratioX,ratioY)
                .setActivityTitle("Event Icon")
                .setActivityMenuIconColor(com.kofze.eventman.R.color.design_default_color_secondary)
                .setBackgroundColor(com.kofze.eventman.R.color.design_default_color_background)
                .setGuidelinesColor(R.color.design_default_color_background)
                .start(SignupActivity.this);
    }

    // create method signUp
    public void signUp() {
        // get the values from the EditTexts
        String name = nameBox.getEditText().getText().toString().trim();
        String email = emailBox.getEditText().getText().toString().trim();
        String password = passwordBox.getEditText().getText().toString().trim();
        String confirmPassword = confirmPasswordBox.getEditText().getText().toString().trim();

        // check if the value is provided
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword)) {
            // check if both password matches
            if (password.equals(confirmPassword)) {
                // create a new user
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // user created
                                    // upload the profile image
                                    uploadImage(firebaseAuth.getCurrentUser());
                                } else {
                                    // error occurred
                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                // password does not match
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            }
        } else {
            // user did not entered all the details
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage(FirebaseUser currentUser) {
        if (logoUri != null) {

            final StorageReference ref = firebaseStorage.getReference().child("images/users/" + currentUser.getUid());
            ref.putFile(logoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // get the profile image url
                                    String imageUrl = uri.toString();
                                    // set the profile image url
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(Uri.parse(imageUrl))
                                            .build();

                                    // go to main activity
                                    currentUser.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // user profile updated
                                                    // go to main activity
                                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                    finish();
                                                } else {
                                                    // error occurred
                                                    Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    );
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
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
                logoUri = activityResult.getUri();
                profile.setImageURI(logoUri);

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