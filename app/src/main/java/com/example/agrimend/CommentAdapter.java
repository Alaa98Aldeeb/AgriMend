package com.example.agrimend;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CommentAdapter extends  RecyclerView.Adapter<CommentAdapter.CommentViewHolder>{
    private Context mContext;
    private List<Comment> mData;
    private boolean click = false;


    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment,parent,false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {

            if(mData.get(position).getUimg() == null){
                Glide.with(mContext).load(mData.get(position).getDefaultImg()).apply(RequestOptions.circleCropTransform()).into(holder.img_user);
            }else{
                Glide.with(mContext).load(mData.get(position).getUimg()).apply(RequestOptions.circleCropTransform()).into(holder.img_user);
            }
            holder.tv_name.setText(mData.get(position).getUname());
            holder.tv_content.setText(mData.get(position).getContent());
            holder.tv_experience.setText(mData.get(position).getUexpert());
            holder.tv_date.setText(mData.get(position).getTimestamp());

            if (mData.get(position).getClick()){
                holder.ibCheckImg.setImageResource(R.drawable.resize_check_solved_icon);
                holder.tv_Check.setText("Solution");
                holder.tv_Check.setTextColor(Color.parseColor("#4caf50"));
            }else {
                holder.ibCheckImg.setImageResource(R.drawable.resize_check_icon);
                holder.tv_Check.setText("Solution");
            }
            holder.section.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.ibCheckImg.setImageResource(R.drawable.resize_check_solved_icon);
                    holder.tv_Check.setTextColor(Color.parseColor("#4caf50"));
                    DatabaseReference comments = FirebaseDatabase.getInstance().getReference("Comments");
                    comments.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot root : dataSnapshot.getChildren()){
                                for (DataSnapshot data : root.getChildren()){
                                    if (data.getKey().equals(mData.get(position).getCommentKey())){
                                        data.child("click").getRef().setValue(true);
                                        notifyItemChanged(position);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        ImageView img_user;
        TextView tv_name,tv_content,tv_date, tv_experience, tv_Check;
        ImageButton ibCheckImg;
        LinearLayout section;

        public CommentViewHolder(final View itemView) {
            super(itemView);
            img_user = itemView.findViewById(R.id.comment_user_img);
            tv_name = itemView.findViewById(R.id.comment_username);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_date = itemView.findViewById(R.id.comment_date);
            tv_experience = itemView.findViewById(R.id.comment_userexpert);
            tv_Check = itemView.findViewById(R.id.checkText);
            ibCheckImg = itemView.findViewById(R.id.checkImg);
            section = itemView.findViewById(R.id.solvedSection);

        }
    }
    /*
    private String timestampToString(long timestamp) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp );
        return DateFormat.format("hh:mm", calendar).toString();

    }

     */
}
