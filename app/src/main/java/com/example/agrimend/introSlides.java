package com.example.agrimend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import maes.tech.intentanim.CustomIntent;

public class introSlides extends AppCompatActivity {

    ViewFlipper viewFlipper;
    RelativeLayout view;
    Button btnNext, btnSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_slides);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        viewFlipper = findViewById(R.id.viewSlides);
        view = findViewById(R.id.firstView);
        btnNext = findViewById(R.id.nextButton);
        btnSkip = findViewById(R.id.skipButton);
        viewFlipper.setFlipInterval(2000);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (viewFlipper.getDisplayedChild() == 3){
                    viewFlipper.setInAnimation(introSlides.this, R.anim.slide_in);
                    viewFlipper.setOutAnimation(introSlides.this, R.anim.slide_out);
                    btnNext.setText("Finish");
                    viewFlipper.showNext();

                }else if (viewFlipper.getDisplayedChild() == 4){
                    startActivity(new Intent(introSlides.this,signup_page.class));
                    finish();
                    introSlides.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                }
                else{
                    viewFlipper.setInAnimation(introSlides.this, R.anim.slide_in);
                    viewFlipper.setOutAnimation(introSlides.this, R.anim.slide_out);
                    viewFlipper.showNext();
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(introSlides.this,signup_page.class));
                finish();
                introSlides.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });
    }
}
