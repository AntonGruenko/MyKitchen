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
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.noDb.NoDb;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<Recipe> recipeList;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.recipeList = recipeList;
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
        ((RecipeHolder)holder).nameText.setText(recipe.getName());
        ((RecipeHolder)holder).timeText.setText(String.valueOf(recipe.getTime()));
        ((RecipeHolder)holder).kcalText.setText(String.valueOf(recipe.getKcal()));
        ((RecipeHolder)holder).likesText.setText(String.valueOf(recipe.getLikes()));
    }

    @Override
    public int getItemCount() { return NoDb.RECIPE_LIST.size(); }

    private class RecipeHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView
                nameText,
                timeText,
                kcalText,
                likesText;


        public RecipeHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_picture);
            nameText = itemView.findViewById(R.id.item_name);
            kcalText = itemView.findViewById(R.id.item_kcal);
            timeText = itemView.findViewById(R.id.item_time);
            likesText = itemView.findViewById(R.id.item_likes);
        }

    }
}
