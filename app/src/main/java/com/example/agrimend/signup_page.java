package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.regex.Pattern;

import maes.tech.intentanim.CustomIntent;

public class signup_page extends AppCompatActivity {

    EditText editText1, editText2, editText3, editText4, editText5;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private static final String USER = "UsersAccounts";
    private String name, email, password, location, experience;
    private userAccount userAccount;
    Button btnRegister;
    ImageView imageUserProfile;
    static int PReqCose = 1, REQUESCODE ;
    Uri pickedImageUri;
    StorageReference mStorage;
    StorageReference imageFilePath;
    LoadingDialog loadingDialog;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data!= null){
                pickedImageUri = data.getData();
                Glide.with(this).load(pickedImageUri).apply(RequestOptions.circleCropTransform())
                        .into(imageUserProfile);
                imageUserProfile.setImageURI(pickedImageUri);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadingDialog = new LoadingDialog(signup_page.this);
        imageUserProfile = findViewById(R.id.userProfilePhoto);
         btnRegister = (Button)findViewById(R.id.register);

        imageUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestPermission();
                }else {
                    openGallery();
                }
            }
        });

        editText1 = (EditText)findViewById(R.id.username);
        editText2 = (EditText)findViewById(R.id.location);
        editText3 = (EditText)findViewById(R.id.experience);
        editText4 = (EditText)findViewById(R.id.email);
        editText5 = (EditText)findViewById(R.id.pass);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(USER);
        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             name = editText1.getText().toString();
             location = editText2.getText().toString();
             experience = editText3.getText().toString();
             email = editText4.getText().toString();
             password = editText5.getText().toString();

             if(name.isEmpty() || location.isEmpty() ||  experience.isEmpty() || email.isEmpty() || password.isEmpty()){
                 showMessage("Please fill all fields");

                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                 showMessage("Invalid Email Address");
             }else{
                 CreateUserAccount(name,location,experience,email,password);
                }

            }
        });
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signup_page.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
    }

    private void checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(signup_page.this, Manifest.permission.READ_EXTERNAL_STORAGE)
            !=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(signup_page.this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(signup_page.this,"Please Accept For Required Permission",Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(signup_page.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCose);
            }
        }
        else {
            openGallery();
        }
    }

    private void CreateUserAccount(final String name, final String location, final String experience, final String email, final String password) {
        loadingDialog.startLoadingDialog();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            firebaseDatabase = FirebaseDatabase.getInstance();
                            databaseReference = firebaseDatabase.getReference("UsersAccounts");
                            firebaseUser = mAuth.getCurrentUser();

                            final String keyId = firebaseUser.getUid();
                            userAccount = new userAccount(name,location,experience,email,password);
                            databaseReference.child(keyId).setValue(userAccount);
                            if (pickedImageUri!=null){
                                updateUserInfo(name, pickedImageUri, mAuth.getCurrentUser());
                                firebaseUser.sendEmailVerification();
                            }else {
                                updateUserInfo(name, mAuth.getCurrentUser());
                                firebaseUser.sendEmailVerification();
                            }

                        }else {
                            refrech();
                            try{
                                throw task.getException();
                            }
                            catch (FirebaseAuthUserCollisionException e) {
                                loadingDialog.dismissDialog();
                                showMessage("Email already exsit");
                                startActivity(new Intent(signup_page.this, login_page.class));
                            }
                            catch (Exception ex){
                                refrech();
                                loadingDialog.dismissDialog();
                                showMessage("Account Creation Failed" + task.getException().getMessage());
                                editText1.setText("");
                                editText2.setText("");
                                editText3.setText("");
                                editText4.setText("");
                                editText5.setText("");
                            }
                        }
                    }
                });
    }

    public void refrech(){
        Intent intent = getIntent();
        overridePendingTransition(0,0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0,0);
        startActivity(intent);
    }

    private void updateUserInfo(String name, FirebaseUser currentUser) {
        loadingDialog.startLoadingDialog();
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name)
                .build();
        currentUser.updateProfile(profileUpdate)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            UpdateUI();
                        }
                    }
                });
    }

    private void updateUserInfo(final String name, Uri pickedImageUri, final FirebaseUser currentUser) {

        mStorage = FirebaseStorage.getInstance().getReference().child("UsersPictures");
        imageFilePath = mStorage.child(pickedImageUri.getLastPathSegment());
        imageFilePath.putFile(pickedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
              imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                      UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name)
                              .setPhotoUri(uri).build();
                      currentUser.updateProfile(profileUpdate)
                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful()){
                                          UpdateUI();
                                      }
                                  }
                              });
                  }
              });
            }
        });

    }

    private void UpdateUI() {
        Intent i = new Intent(signup_page.this,home_news.class);
        loadingDialog.dismissDialog();
        startActivity(i);
        showMessage("Account Created Sucessfully, Chech Your Email To Verify Account.");
        finish();
        signup_page.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
        mAuth.signOut();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
