package com.example.uniorproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniorproject.R;
import com.example.uniorproject.noDb.NoDb;

import java.util.List;

public class SelectedTagsAdapter extends RecyclerView.Adapter<SelectedTagsAdapter.SelectedTagsAdapterHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<String> tagList;

    public SelectedTagsAdapter(Context context, List<String> tagList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.tagList = tagList;
    }

    @NonNull
    @Override
    public SelectedTagsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.selected_tag_item, parent, false);
        return new SelectedTagsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedTagsAdapterHolder holder, int position) {
        ((SelectedTagsAdapterHolder)holder).deleteButton.setText(tagList.get(position));
        ((SelectedTagsAdapterHolder)holder).deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    static class SelectedTagsAdapterHolder extends RecyclerView.ViewHolder{
        private Button deleteButton;
        public SelectedTagsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
