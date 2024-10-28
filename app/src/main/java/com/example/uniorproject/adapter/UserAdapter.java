package com.example.uniorproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uniorproject.R;
import com.example.uniorproject.domain.User;


import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final List<User> users;
    private final LayoutInflater inflater;
    private final UserClickListener clickListener;

    public UserAdapter(Context context, List<User> users, UserClickListener clickListener) {
        this.context = context;
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.userNameText.setText(user.getName());
        if(user.getProfilePic().isEmpty()){
            holder.userProfilePic.setImageResource(R.drawable.ic_baseline_person_24);
        }
        else{
            Glide.
                    with(context).
                    load(user.getProfilePic()).
                    centerCrop().
                    into(holder.userProfilePic);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onClick(user.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        private final TextView userNameText;
        private final ImageView userProfilePic;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameText = itemView.findViewById(R.id.user_name);
            userProfilePic = itemView.findViewById(R.id.user_profile_pic);
        }
    }

    public interface UserClickListener{
        void onClick(int position);
    }
}
