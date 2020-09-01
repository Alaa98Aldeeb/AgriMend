package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class CommunityHome extends AppCompatActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            pickedImgUri = data.getData() ;
            popupPostImage.setImageURI(pickedImgUri);
        }
    }
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;
    Dialog popAddPost ;
    ImageView popupUserImage,popupPostImage,popupAddBtn;
    EditText popupTitle,popupDescription, popupPlantName;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;
    FirebaseDatabase database;
    DatabaseReference myRef, userDatabase;
    String currentUserLocation;
    LayoutInflater inflater;
    View view;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        int id = item.getItemId();
        if (id==R.id.userProfile){
            if (firebaseUser!=null){
                startActivity(new Intent(CommunityHome.this,profileUser.class));
                finish();
                CommunityHome.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else{
                startActivity(new Intent(CommunityHome.this,login_page.class));
                finish();
                CommunityHome.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }

        }if (id==R.id.dissComm){
            startActivity(new Intent(CommunityHome.this,PostsLists.class));
            finish();
            CommunityHome.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            return true;
        }if (id==R.id.testPlant) {
            startActivity(new Intent(CommunityHome.this,DiagnosePlant.class));
            CommunityHome.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            finish();
            return true;

        }if (id==R.id.news){
            startActivity(new Intent(CommunityHome.this,home_news.class));
            finish();
            CommunityHome.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            return true;
        }
        if (id==R.id.comm){
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_home);
        //inflater = getLayoutInflater();
        //view = inflater.inflate(R.layout.popup_add_post,null);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String uId = firebaseUser.getUid();
        //defaultImageHint = findViewById(R.id.default_text_add_image_post);
        userDatabase = FirebaseDatabase.getInstance().getReference("UsersAccounts").child(uId);
        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              currentUserLocation = dataSnapshot.child("userLocation").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // ini popup
        iniPopup();
        setupPopupImageClick();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { popAddPost.show(); }});
    }

    private void setupPopupImageClick() {
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(CommunityHome.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CommunityHome.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(CommunityHome.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            }

            else
            {
                ActivityCompat.requestPermissions(CommunityHome.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();
    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void iniPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;


        // ini popup widgets
        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupPlantName = popAddPost.findViewById(R.id.popup_plant_name);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        // load Current user profile photo

        if (firebaseUser.getPhotoUrl() != null){
            Glide.with(CommunityHome.this).load(firebaseUser.getPhotoUrl()).apply(RequestOptions.circleCropTransform())
                    .into(popupUserImage);
        }else {
            Glide.with(CommunityHome.this).load(R.drawable.userimage).apply(RequestOptions.circleCropTransform())
                    .into(popupUserImage);
        }

        // Add post click Listener
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                // we need to test all input fields (Title and description ) and post image

                if (!popupTitle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && pickedImgUri != null ) {

                    //everything is okey no empty or null value
                    // TODO Create Post Object and add it to firebase database
                    // first we need to upload post Image
                    // access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Disscussion Community");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownlaodLink = uri.toString();
                                    // create post Object

                                    int defaultImg = R.drawable.userimage;
                                    Date currentTime = Calendar.getInstance().getTime();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                    String date = dateFormat.format(currentTime);
                                    if (firebaseUser.getPhotoUrl() != null){
                                        Post post = new Post(popupTitle.getText().toString(),
                                                popupDescription.getText().toString(),
                                                popupPlantName.getText().toString(),
                                                imageDownlaodLink,
                                                firebaseUser.getUid(),
                                                firebaseUser.getDisplayName(),
                                                firebaseUser.getPhotoUrl().toString(),
                                                currentUserLocation, date);
                                        addPost(post);
                                    }else {
                                        Post post = new Post(popupTitle.getText().toString(),
                                                popupDescription.getText().toString(),
                                                popupPlantName.getText().toString(),
                                                imageDownlaodLink,
                                                firebaseUser.getUid(),
                                                firebaseUser.getDisplayName(),
                                                defaultImg,
                                                currentUserLocation, date);
                                        addPost(post);
                                    }


                                    // Add post to firebase database

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);

                                }
                            });
                        }
                    });
                }
                else {
                    showMessage("Please verify all input fields and choose Post Image") ;
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CommunityHome.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
    }

    private void addPost(Post post) {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Posts").push();
        String key = myRef.getKey();
        post.setPostKey(key);


        // add post data to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
                startActivity(new Intent(CommunityHome.this,PostsLists.class));
                CustomIntent.customType(CommunityHome.this, "left-to-right");
                finish();
            }
        });
    }
    private void showMessage(String message) {
        Toast.makeText(CommunityHome.this,message,Toast.LENGTH_LONG).show();
    }
}
