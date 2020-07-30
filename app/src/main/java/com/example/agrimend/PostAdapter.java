package com.example.agrimend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder>  {
    private Context mContext;
    private List<Post> mData ;


    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvLocation.setText(mData.get(position).getUserLocation());
        holder.tvTitle.setText(mData.get(position).getTitle());
        holder.tvName.setText(mData.get(position).getUserName());
        holder.tvPlantName.setText(mData.get(position).getPlantName());
        Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);
        if (mData.get(position).getUserPhoto() != null){
            Glide.with(mContext).load(mData.get(position).getUserPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.imgPostProfile);
        }else {
            Glide.with(mContext).load(mData.get(position).getDefaultImg()).apply(RequestOptions.circleCropTransform()).into(holder.imgPostProfile);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvName;
        TextView tvLocation;
        TextView tvPlantName;
        ImageView imgPost;
        ImageView imgPostProfile;

       public MyViewHolder(View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.row_post_title);
            tvName = itemView.findViewById(R.id.row_post_profile_name);
            tvPlantName = itemView.findViewById(R.id.row_post_plant_name);
            tvLocation = itemView.findViewById(R.id.row_post_location);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent postDetailActivity = new Intent(mContext,PostDetails.class);
                    int position = getAdapterPosition();

                        postDetailActivity.putExtra("title",mData.get(position).getTitle());
                        postDetailActivity.putExtra("postImage",mData.get(position).getPicture());
                        postDetailActivity.putExtra("description",mData.get(position).getDescription());
                        postDetailActivity.putExtra("postKey",mData.get(position).getPostKey());
                        postDetailActivity.putExtra("userPhoto",mData.get(position).getUserPhoto());
                        postDetailActivity.putExtra("userName",mData.get(position).getUserName());
                        postDetailActivity.putExtra("location",mData.get(position).getUserLocation());
                        postDetailActivity.putExtra("plantName",mData.get(position).getPlantName());
                        postDetailActivity.putExtra("postDate",mData.get(position).getTimeStamp()) ;
                    mContext.startActivity(postDetailActivity);
                }
            });

        }


    }
}
