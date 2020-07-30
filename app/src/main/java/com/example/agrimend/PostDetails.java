package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import maes.tech.intentanim.CustomIntent;

import static com.example.agrimend.R.id.post_detail_add_comment_btn;
import static com.example.agrimend.R.id.post_detail_comment;
import static com.example.agrimend.R.id.post_detail_date_name;
import static com.example.agrimend.R.id.post_detail_desc;
import static com.example.agrimend.R.id.post_detail_title;
import static com.example.agrimend.R.id.post_detail_userName;

public class PostDetails extends AppCompatActivity {

    public ImageView imgPost ,imgUserPost;
    public TextView txtPostDesc, txtPostDateName, txtPostTitle, txtPostUserName;
    public EditText editTextComment;
    public ImageButton btnAddComment;
    public String PostKey, userExpert , uid ;
    public FirebaseAuth firebaseAuth ;
    public FirebaseUser firebaseUser ;
    public FirebaseDatabase firebaseDatabase;
    public RecyclerView RvComment;
    public CommentAdapter commentAdapter;
    List<Comment> listComment;
    public static String COMMENT_KEY = "Comments" ;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PostDetails.this.overridePendingTransition(R.anim.bottom_to_up,R.anim.up_to_bottom);
        //getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_details);
        PostDetails.this.overridePendingTransition(R.anim.bottom_to_up,R.anim.up_to_bottom);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            uid = firebaseUser.getUid();

            imgPost =  findViewById(R.id.post_detail_img);
            imgUserPost = findViewById(R.id.post_detail_user_img);
            txtPostDesc = findViewById(post_detail_desc);
            txtPostDateName = findViewById(post_detail_date_name);
            txtPostTitle = findViewById(post_detail_title);
            txtPostUserName = findViewById(post_detail_userName);
            editTextComment = findViewById(post_detail_comment);
            btnAddComment = findViewById(post_detail_add_comment_btn);
            RvComment = findViewById(R.id.rv_comment);

            editTextComment.bringToFront();
        DatabaseReference userInfo = firebaseDatabase.getReference("UsersAccounts").child(uid);
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userExpert=String.valueOf(dataSnapshot.child("userExperience").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
                String commentKey = commentReference.getKey();
                String comment_content = editTextComment.getText().toString();
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                int img = R.drawable.resize_check_icon;
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                String date = dateFormat.format(currentTime);
                Comment comment;

                if (firebaseUser.getPhotoUrl() != null){
                    String uimg;
                    uimg = firebaseUser.getPhotoUrl().toString();
                    comment = new Comment(comment_content,uid,uimg,uname, userExpert,date,commentKey,img,false);

                }else {
                    int replaceImage = R.drawable.userimage;
                   comment = new Comment(comment_content,uid,replaceImage,uname, userExpert,date,commentKey,img,false);
                }
                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("comment added");
                        editTextComment.setText("");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("fail to add comment : "+e.getMessage());
                    }
                });
            }
        });


        // now we need to bind all data into those views
        // firt we need to get post data
        // we need to send post detail data to this activity first ...
        // now we can get post data

        String postImage = getIntent().getExtras().getString("postImage") ;
        Glide.with(this).load(postImage).into(imgPost);

        String postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

        String userpostImage = getIntent().getExtras().getString("userPhoto");
        if (userpostImage != null){
            Glide.with(this).load(userpostImage).apply(RequestOptions.circleCropTransform()).into(imgUserPost);
        }else {
            Glide.with(this).load(R.drawable.userimage).apply(RequestOptions.circleCropTransform()).into(imgUserPost);
        }


        String userName = getIntent().getExtras().getString("userName");
        txtPostUserName.setText(userName);

        String postDescription = getIntent().getExtras().getString("description");
        txtPostDesc.setText(postDescription);

        String date = getIntent().getExtras().getString("postDate");
        txtPostDateName.setText(date);

        PostKey = getIntent().getExtras().getString("postKey");

        // ini Recyclerview Comment
        iniRvComment();
    }
/*
    private String timestampToString(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd-MM-yyyy", calendar).toString();
    }
*/

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {

                    final Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment) ;
                }

                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
