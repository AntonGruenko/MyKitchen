package com.example.uniorproject.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.uniorproject.R;
import com.example.uniorproject.databinding.RecievedMessageItemBinding;
import com.example.uniorproject.domain.Message;
import com.example.uniorproject.domain.User;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<Message> messages;
    private final User sender;
    private final LayoutInflater inflater;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(Context context, List<Message> messages, User sender) {
        this.context = context;
        this.messages = messages;
        this.sender = sender;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT){
            return new SentMessageViewHolder(inflater.inflate(R.layout.sent_message_item, parent, false));
        }
        else{
            return new ReceivedMessageViewHolder(inflater.inflate(R.layout.recieved_message_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();
        Message message = messages.get(pos);
        if(getItemViewType(pos) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).messageText.setText(message.getText());
            if(message.getSender().getProfilePic().isEmpty()){
                ((SentMessageViewHolder) holder).profilePic.setImageResource(R.drawable.ic_baseline_person_24);
            }
            else {
                Glide.with(context).load(message.getSender().getProfilePic()).centerCrop().into(((SentMessageViewHolder) holder).profilePic);
            }
        }
        else{
            ((ReceivedMessageViewHolder) holder).messageText.setText(message.getText());
            if(message.getSender().getProfilePic().isEmpty()){
                ((ReceivedMessageViewHolder) holder).profilePic.setImageResource(R.drawable.ic_baseline_person_24);
            }
            else {

                Glide.with(context)
                        .load(message.getSender().getProfilePic())
                        .centerCrop()
                        .into(((ReceivedMessageViewHolder) holder).profilePic);
            }
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getSender().getId() == sender.getId()){
            return VIEW_TYPE_SENT;
        }
        else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final TextView messageText;
        private final ImageView profilePic;


        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.user_profile_pic);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        private final TextView messageText;
        private final ImageView profilePic;


        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.user_profile_pic);
            messageText = itemView.findViewById(R.id.message_text);
        }
    }
}
