package com.example.uniorproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniorproject.R;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.noDb.NoDb;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Recipe> recipeList;
    private final OnRecipeClickListener listener;
    private final List<Picture> pictureList;

    public RecipeFeedAdapter(Context context, List<Recipe> recipeList, OnRecipeClickListener listener, List<Picture> pictureList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.recipeList = recipeList;
        this.listener = listener;
        this.pictureList = pictureList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recipe_item, parent, false);

        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = NoDb.RECIPE_LIST.get(position);
        int minutes, hours;
        hours = recipe.getTime()/60;
        minutes = recipe.getTime() - hours * 60;
        ((RecipeHolder)holder).authorNameText.setText(recipe.getAuthor().getName());
        ((RecipeHolder)holder).nameText.setText(recipe.getName());
        ((RecipeHolder)holder).timeText.setText(String.format("%d:%d", hours, minutes));
        ((RecipeHolder)holder).kcalText.setText(String.valueOf(recipe.getKcal()));
        ((RecipeHolder)holder).likesText.setText(String.valueOf(recipe.getLikes()));
        try {
            String link = pictureList.get(position).getLink();
            if(!link.isEmpty()) {
                Picasso.with(context).load(link).into(((RecipeHolder) holder).imageView);
            }
            else {

            }
        }
        catch (IndexOutOfBoundsException e){}

        ((RecipeHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(recipe, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() { return NoDb.RECIPE_LIST.size(); }

    private class RecipeHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView
                authorNameText,
                nameText,
                timeText,
                kcalText,
                likesText;


        public RecipeHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_picture);
            authorNameText = itemView.findViewById(R.id.item_author_name);
            nameText = itemView.findViewById(R.id.item_name);
            kcalText = itemView.findViewById(R.id.item_kcal);
            timeText = itemView.findViewById(R.id.item_time);
            likesText = itemView.findViewById(R.id.item_likes);
        }

    }

    public interface OnRecipeClickListener{
        void onClick(Recipe recipe, int position);
    }
}
