package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import maes.tech.intentanim.CustomIntent;


public class news_list_details extends AppCompatActivity {

    TextView textViewTitle, textViewDescription, textViewDate;
    ImageView imageView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
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
                startActivity(new Intent(news_list_details.this,profileUser.class));
                finish();
                news_list_details.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else{
                startActivity(new Intent(news_list_details.this,login_page.class));
                finish();
                news_list_details.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }

        }if (id==R.id.dissComm){
            if (firebaseUser!= null){
                startActivity(new Intent(news_list_details.this,PostsLists.class));
                finish();
                news_list_details.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else {
                startActivity(new Intent(news_list_details.this,login_page.class));
                finish();
                news_list_details.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }

        }if (id==R.id.testPlant) {
            startActivity(new Intent(news_list_details.this,DiagnosePlant.class));
            news_list_details.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            finish();
            return true;

        }if (id==R.id.news){
            startActivity(new Intent(news_list_details.this,home_news.class));
            finish();
            news_list_details.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            return true;
        }
        if (id==R.id.comm){
            if (firebaseUser!= null){
                startActivity(new Intent(news_list_details.this,CommunityHome.class));
                finish();
                news_list_details.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else {
                startActivity(new Intent(news_list_details.this,login_page.class));
                finish();
                news_list_details.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
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
    public void onBackPressed() {
        super.onBackPressed();
        news_list_details.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_list_details);
        news_list_details.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);

        progressBar = findViewById(R.id.newListDetailsProgressBar);
        imageView = findViewById(R.id.imageViewDetails);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDescription = findViewById(R.id.textViewDesc);
        textViewDate = findViewById(R.id.textViewDate);
        textViewTitle.setText(getIntent().getStringExtra("title"));
        textViewDescription.setText(getIntent().getStringExtra("description"));
        textViewDate.setText(dateFormat(getIntent().getStringExtra("date")));

        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(getIntent().getStringExtra("image")).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public String dateFormat(String pugD){
        Date date = new Date(pugD);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MMMM-yyyy");
        try {
            date = sdf.parse(pugD);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return sdf.format(date);
        }
    }
}
