package com.example.agrimend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import maes.tech.intentanim.CustomIntent;

public class startup_page extends AppCompatActivity {

    private static int TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final View myLayout = findViewById(R.id.start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .getBoolean("isFirstRun", true);

                if (isFirstRun) {
                    //show sign up activity
                    startActivity(new Intent(startup_page.this, introSlides.class));
                    finish();
                    startup_page.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                }else {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if (firebaseUser!=null){
                        startActivity(new Intent(startup_page.this, home_news.class));
                        finish();
                        startup_page.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }
                    else {
                        startActivity(new Intent(startup_page.this, login_page.class));
                        finish();
                        startup_page.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                    }

                }


                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                        .putBoolean("isFirstRun", false).commit();
            }
        }, TIME_OUT);
    }
}
