package com.example.agrimend;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyRecycleAapter extends RecyclerView.Adapter<MyRecycleAapter.MyViewHolder> {

    private Context ctx;
    private MySingleton mySingleton;
    private ImageLoader mImageLoader;

    public MyRecycleAapter(Context ctx, ArrayList<newsModel> nList) {
        this.ctx = ctx;
        this.nList = nList;
        mySingleton = MySingleton.getInstance(ctx);
        mImageLoader = mySingleton.getImageLoader();
    }

    private ArrayList<newsModel> nList;

    @NonNull
    @Override
    public MyRecycleAapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(ctx.getApplicationContext()).inflate(R.layout.news_list,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyRecycleAapter.MyViewHolder holder, int position) {

        newsModel nModel = nList.get(position);
        holder.title.setText(nModel.getTitle());
        holder.progressBar.setVisibility(View.VISIBLE);
        //holder.des.setText(nModel.getDescription());
        //holder.date.setText(nModel.getPugData());

        String imgurl = nModel.getImageUrl();
        if(imgurl !=null){
            mImageLoader.get(imgurl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.progressBar.setVisibility(View.VISIBLE);
                    Glide.with(ctx).load(response.getBitmap()).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                           holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(holder.img);
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return nList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img;
        private TextView title;
        private ProgressBar progressBar;
        private TextView des;
        private TextView date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView)itemView.findViewById(R.id.imageView);
            title = (TextView)itemView.findViewById(R.id.title);
            progressBar = (ProgressBar) itemView.findViewById(R.id.newsListProgressBar);
            //des = (TextView)itemView.findViewById(R.id.textViewDesc);
            //date = (TextView)itemView.findViewById(R.id.textViewDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            newsModel item = nList.get(position);
            Intent intent = new Intent(ctx, news_list_details.class);
            intent.putExtra("title",item.getTitle());
            intent.putExtra("image",item.getImageUrl());
            intent.putExtra("description",item.getDescription());
            intent.putExtra("date",item.getPugData());
            ctx.startActivity(intent);
        }

    }
}
