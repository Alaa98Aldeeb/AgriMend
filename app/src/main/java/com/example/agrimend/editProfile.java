package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class editProfile extends AppCompatActivity {

    EditText currentNewUserName, currentNewUserEmail, currentNewUserLocation, currentNewUserPassword;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView newUserProfileImage;
    Button btnEdit;
    String newUserName, newUserPass, newUserLocation, newUserEmail;
    String currentUserId, currentUserExperience;
    LoadingDialog loadingDialog;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.userProfile){
            return true;
        }if (id==R.id.dissComm){
            startActivity(new Intent(editProfile.this,PostsLists.class));
            editProfile.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            return true;
        }if (id==R.id.testPlant) {
            startActivity(new Intent(editProfile.this,DiagnosePlant.class));
            editProfile.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            finish();
            return true;

        }if (id==R.id.news){
            startActivity(new Intent(editProfile.this,home_news.class));
            editProfile.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            return true;
        }
        if (id==R.id.comm){
            startActivity(new Intent(editProfile.this,CommunityHome.class));
            editProfile.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            return true;
        }
        if (id==R.id.share){
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("Text/plain");
            String shareBody = "Your Body here";
            String shareSub = "Your Subject here";
            myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
            myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
            startActivity(Intent.createChooser(myIntent,""));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        editProfile.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        loadingDialog = new LoadingDialog(editProfile.this);
        currentNewUserName = (EditText) findViewById(R.id.newUserName);
        currentNewUserEmail = (EditText) findViewById(R.id.newUserEmail);
        currentNewUserLocation = (EditText) findViewById(R.id.newUserLocation);
        currentNewUserPassword = (EditText) findViewById(R.id.newUserPassword);
        newUserProfileImage = findViewById(R.id.newUserPhoto);
        btnEdit = (Button)findViewById(R.id.editProfileData);;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currentUserId = firebaseUser.getUid();
        currentNewUserEmail.setText(firebaseUser.getEmail());
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference().child("UsersAccounts").child(firebaseUser.getUid());

        if (firebaseUser.getPhotoUrl()==null){
            Glide.with(this).load(R.drawable.userimage).into(newUserProfileImage);

        }else{
            Glide.with(this).load(firebaseUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform())
                    .into(newUserProfileImage);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("userName").getValue().toString();
                String location = dataSnapshot.child("userLocation").getValue().toString();
                String pass = dataSnapshot.child("userPassword").getValue().toString();
                currentUserExperience = dataSnapshot.child("userExperience").getValue().toString();

                currentNewUserName.setText(name);
                currentNewUserLocation.setText(location);
                currentNewUserPassword.setText(pass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingDialog.startLoadingDialog();
                newUserName = currentNewUserName.getText().toString();
                newUserPass = currentNewUserPassword.getText().toString();
                newUserEmail = currentNewUserEmail.getText().toString();
                newUserLocation = currentNewUserLocation.getText().toString();

                userAccount newUserData = new userAccount(newUserName,newUserLocation,currentUserExperience,newUserEmail,newUserPass);
                databaseReference.setValue(newUserData);
                final UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(newUserName)
                        .setPhotoUri(firebaseUser.getPhotoUrl()).build();
                firebaseUser.updateProfile(profileUpdate);

               DatabaseReference posts = FirebaseDatabase.getInstance().getReference("Posts");
               posts.orderByChild("userId").equalTo(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for (DataSnapshot data : dataSnapshot.getChildren()){
                           data.child("userName").getRef().setValue(newUserName);
                           data.child("userLocation").getRef().setValue(newUserLocation);
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

                final DatabaseReference comments = FirebaseDatabase.getInstance().getReference("Comments");
                comments.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot root :dataSnapshot.getChildren()){
                            for (final DataSnapshot data : root.getChildren()){
                                if (data.child("uid").getValue().equals(firebaseAuth.getUid())) {
                                    data.child("uname").getRef().setValue(newUserName);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                AuthCredential credential = EmailAuthProvider.getCredential(newUserEmail,newUserPass);

                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseUser.updateEmail(newUserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    loadingDialog.dismissDialog();
                                    Toast.makeText(getApplicationContext(),"Profile Updated Succefully",
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(editProfile.this,profileUser.class));
                                    editProfile.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
                                    finish();
                                }
                            }
                        });
                    }
                });
            }
        });

    }

}
