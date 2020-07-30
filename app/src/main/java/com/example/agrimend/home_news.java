package com.example.agrimend;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import maes.tech.intentanim.CustomIntent;

public class home_news extends AppCompatActivity{


    private RecyclerView recyclerView;
    private ArrayList<newsModel> newList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ProgressBar myProgressBar;
    private MyRecycleAapter myRecycleAapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }
    private RequestQueue mQue;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        int id = item.getItemId();
        if (id==R.id.userProfile){
            if (firebaseUser!=null){
                startActivity(new Intent(home_news.this,profileUser.class));
                home_news.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else{
                startActivity(new Intent(home_news.this,login_page.class));
                home_news.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                finish();
                return true;
            }

        }if (id==R.id.dissComm){
            if (firebaseUser!=null){
                startActivity(new Intent(home_news.this,PostsLists.class));
                home_news.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else{
                startActivity(new Intent(home_news.this,login_page.class));
                home_news.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                finish();
                return true;
            }

        }if (id==R.id.testPlant) {
            startActivity(new Intent(home_news.this,DiagnosePlant.class));
            home_news.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            finish();
            return true;

        }if (id==R.id.news){
            return true;
        }
        if (id==R.id.comm){
            if (firebaseUser!= null){
                startActivity(new Intent(home_news.this,CommunityHome.class));
                home_news.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                return true;
            }else {
                startActivity(new Intent(home_news.this,login_page.class));
                home_news.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
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
        home_news.this.overridePendingTransition(R.anim.right_to_left,R.anim.left_to_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_news);

        home_news.this.overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);

        mQue = MySingleton.getInstance(this).getRequestQueue();
        recyclerView = (RecyclerView)findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        myRecycleAapter = new MyRecycleAapter(this,newList);
        recyclerView.setAdapter(myRecycleAapter);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        myProgressBar = findViewById(R.id.progressBar);

        String url = "https://www.forbes.com/sites/daphneewingchow/feed/";

        StringRequest mRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Document doc = Jsoup.parse(response);
                Elements itemElements = doc.select("item");

                for (int i = 0; i<itemElements.size();i++ ){
                    Element item = itemElements.get(i);
                    String title = item.child(1).text();
                    String des = item.child(2).text();
                    String imgLink = item.child(9).attr("url").toString();
                    String pugD = item.child(6).text();

                    newsModel nModel = new newsModel(title, des, pugD, imgLink);
                    myProgressBar.setVisibility(View.GONE);
                    newList.add(nModel);
                }
                myRecycleAapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(this).addToRequestQueue(mRequest);
    }

}
