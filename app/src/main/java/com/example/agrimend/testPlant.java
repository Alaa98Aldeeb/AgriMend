package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class testPlant extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        testPlant.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
    }

    private FirebaseUser firebaseUser;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        int id = item.getItemId();
        if (id==R.id.userProfile){
            if (firebaseUser!=null){
                startActivity(new Intent(testPlant.this,profileUser.class));
                testPlant.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else{
                startActivity(new Intent(testPlant.this,login_page.class));
                finish();
                testPlant.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }

        }if (id==R.id.dissComm){
            if (firebaseUser!= null){
                startActivity(new Intent(testPlant.this,PostsLists.class));
                finish();
                testPlant.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else {
                startActivity(new Intent(testPlant.this,login_page.class));
                finish();
                testPlant.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }

        }if (id==R.id.testPlant) {
            return true;

        }if (id==R.id.news){
            startActivity(new Intent(testPlant.this,home_news.class));
            finish();
            testPlant.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            return true;
        }
        if (id==R.id.comm){
            if (firebaseUser!= null){
                startActivity(new Intent(testPlant.this,CommunityHome.class));
                finish();
                testPlant.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else {
                startActivity(new Intent(testPlant.this,login_page.class));
                finish();
                testPlant.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_plant);
    }
}
