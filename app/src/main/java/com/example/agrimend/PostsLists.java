package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class PostsLists extends AppCompatActivity {

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (id==R.id.userProfile){
            if (firebaseUser!=null){
                startActivity(new Intent(PostsLists.this,profileUser.class));
                PostsLists.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                finish();
                return true;
            }else{
                startActivity(new Intent(PostsLists.this,login_page.class));
                PostsLists.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                finish();
                return true;
            }
        }if (id==R.id.dissComm){
            return true;
        }if (id==R.id.testPlant) {
            startActivity(new Intent(PostsLists.this,DiagnosePlant.class));
            PostsLists.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            return true;

        }if (id==R.id.news){
            startActivity(new Intent(PostsLists.this,home_news.class));
            finish();
            PostsLists.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            return true;
        }
        if (id==R.id.comm){
            startActivity(new Intent(PostsLists.this,CommunityHome.class));
            PostsLists.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            finish();
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

    RecyclerView postRecyclerView ;
    PostAdapter postAdapter ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar myProgressBar;
    List<Post> postList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PostsLists.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_lists);
        PostsLists.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);

            // Inflate the layout for this fragment
            postRecyclerView  = findViewById(R.id.postRV);
            postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            postRecyclerView.setHasFixedSize(true);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("Posts");
            myProgressBar = findViewById(R.id.progressBarCommunity);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                postList = new ArrayList<>();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {

                    Post post = postsnap.getValue(Post.class);
                    myProgressBar.setVisibility(View.GONE);
                    postList.add(post) ;
                }
                postAdapter = new PostAdapter(PostsLists.this,postList);
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
