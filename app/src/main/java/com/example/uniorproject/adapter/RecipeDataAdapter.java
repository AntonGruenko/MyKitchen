package com.example.uniorproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.uniorproject.R;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.noDb.NoDb;


import java.util.List;

public class RecipeDataAdapter extends RecyclerView.Adapter<RecipeDataAdapter.RecipeDataHolder> {
    private final Context context;
    private final List<String> elements;
    private final LayoutInflater inflater;
    private final int contentType;
    private List<Picture> pictures;

    public RecipeDataAdapter(Context context, List<String> elements, int contentType, List<Picture> pictures) {
        this.context = context;
        this.elements = elements;
        this.inflater = LayoutInflater.from(context);
        this.contentType = contentType;
        this.pictures = pictures;
    }

    public RecipeDataAdapter(Context context, List<String> elements, int contentType) {
        this.context = context;
        this.elements = elements;
        this.inflater = LayoutInflater.from(context);
        this.contentType = contentType;
    }

    @NonNull
    @Override
    public RecipeDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_data_item, parent, false);
        return new RecipeDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeDataHolder holder, int position) {
        holder.elementText.setText(String.format("%d. %s",
                holder.getAdapterPosition()+1,
                elements.get(holder.getAdapterPosition())));

        if(contentType == 2) {
            try {
                try {
                    Glide.with(context).load(NoDb.PICTURE_LIST.get(position + 1).getLink()).apply(new RequestOptions().override(768, 576)).centerCrop().into(holder.elementImage);
                }
                catch (IllegalArgumentException e){}
            }
            catch (IndexOutOfBoundsException e){}
        }
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    class RecipeDataHolder extends RecyclerView.ViewHolder{

        TextView elementText;
        ImageView elementImage;

        public RecipeDataHolder(@NonNull View itemView) {
            super(itemView);
            elementText = itemView.findViewById(R.id.item_data);
            elementImage = itemView.findViewById(R.id.recipe_item_picture);
        }
    }
}
