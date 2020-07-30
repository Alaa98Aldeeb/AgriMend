package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

public class profileUser extends AppCompatActivity {

    TextView currentUserName, currentUserEmail, currentUserLocation, currentUserPassword, currentUserExpert;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView userProfile;
    Button btnEdit;
    String name, location, pass, expert;
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
            startActivity(new Intent(profileUser.this,PostsLists.class));
            CustomIntent.customType(this, "left-to-right");
            return true;
        }if (id==R.id.testPlant) {
            startActivity(new Intent(profileUser.this,DiagnosePlant.class));
            CustomIntent.customType(this, "left-to-right");
            return true;

        }if (id==R.id.news){
            startActivity(new Intent(profileUser.this,home_news.class));
            CustomIntent.customType(this, "left-to-right");
            return true;
        }
        if (id==R.id.comm){
            startActivity(new Intent(profileUser.this,CommunityHome.class));
            CustomIntent.customType(this, "left-to-right");
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
        profileUser.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);

        currentUserName = (TextView)findViewById(R.id.userFullName);
        currentUserEmail = (TextView)findViewById(R.id.userFullEmail);
        currentUserLocation = (TextView)findViewById(R.id.userFullLocation);
        currentUserPassword = (TextView)findViewById(R.id.userFullPassword);
        currentUserExpert = (TextView)findViewById(R.id.userExpert);
        userProfile = findViewById(R.id.userPhoto);
        btnEdit = (Button)findViewById(R.id.editProfile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        currentUserEmail.setText(firebaseUser.getEmail());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("UsersAccounts").child(firebaseUser.getUid());

        if (firebaseUser.getPhotoUrl()==null){
            Glide.with(this).load(R.drawable.userimage).into(userProfile);

        }else{
            Glide.with(this).load(firebaseUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform()).into(userProfile);
        }


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name = dataSnapshot.child("userName").getValue().toString();
                    location = dataSnapshot.child("userLocation").getValue().toString();
                    pass = dataSnapshot.child("userPassword").getValue().toString();
                    expert = dataSnapshot.child("userExperience").getValue().toString();
                    currentUserName.setText(name);
                    currentUserLocation.setText(location);
                    currentUserPassword.setText(pass);
                    currentUserExpert.setText(expert);
                    SharedPreferences sharedPreferences = getSharedPreferences("userName",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("name", name);
                    editor.putString("location",location);
                    editor.apply();
                    String uid = firebaseUser.getUid();
                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(name)
                        .setPhotoUri(firebaseUser.getPhotoUrl()).build();
                firebaseUser.updateProfile(profileUpdate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(profileUser.this,editProfile.class));
                profileUser.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });

    }

}
