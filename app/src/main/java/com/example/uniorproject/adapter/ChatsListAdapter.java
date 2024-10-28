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
import com.example.uniorproject.domain.Message;
import com.example.uniorproject.domain.User;


import java.util.List;

public class ChatsListAdapter extends RecyclerView.Adapter<ChatsListAdapter.ChatsViewHolder> {

    private final Context context;
    private final List<Message> messages;
    private final LayoutInflater inflater;
    private final ClickListener listener;
    private final User currentUser;

    public ChatsListAdapter(Context context, List<Message> messages, ClickListener listener, User currentUser) {
        this.context = context;
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
        this.currentUser = currentUser;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatsViewHolder(inflater.inflate(R.layout.chat_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {
        Message message = messages.get(position);
        if(message.getSender().getId() == currentUser.getId()){
            holder.authorText.setText(message.getReceiver().getName());
            if(message.getReceiver().getProfilePic().isEmpty()){
                holder.profilePic.setImageResource(R.drawable.ic_baseline_person_24);
            }
            else{
                Glide.with(context).load(message.getReceiver().getProfilePic()).centerCrop().into(holder.profilePic);
            }
        }
        else {
            holder.authorText.setText(message.getSender().getName());
            if(message.getSender().getProfilePic().isEmpty()){
                holder.profilePic.setImageResource(R.drawable.ic_baseline_person_24);
            }
            else{
                Glide.with(context).load(message.getSender().getProfilePic()).centerCrop().into(holder.profilePic);
            }
        }

        holder.messageText.setText(message.getText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatsViewHolder extends RecyclerView.ViewHolder{
        private final ImageView profilePic;
        private final TextView authorText;
        private final TextView messageText;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.user_profile_pic);
            authorText = itemView.findViewById(R.id.user_name);
            messageText = itemView.findViewById(R.id.last_message);
        }
    }

    public interface ClickListener{
        void onClick(int position);
    }
}
