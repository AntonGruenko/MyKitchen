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

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.domain.Picture;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RecipeFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<Recipe> recipeList;
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
        Recipe recipe = recipeList.get(position);
        int minutes, hours;
        hours = recipe.getTime()/60;
        minutes = recipe.getTime() - hours * 60;
        ((RecipeHolder)holder).authorNameText.setText(recipe.getAuthor().getName());
        ((RecipeHolder)holder).nameText.setText(recipe.getName());
        if(minutes >= 10) {
            ((RecipeHolder) holder).timeText.setText(String.format("%d:%d", hours, minutes));
        }
        else {
            ((RecipeHolder) holder).timeText.setText(String.format("%d:0%d", hours, minutes));
        }
        ((RecipeHolder)holder).kcalText.setText(String.valueOf(recipe.getKcal()));

        new VolleyAPI(context).findRecipeLikesByRecipe(recipe.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String likes = response.getString("num");
                    ((RecipeHolder) holder).likesText.setText(likes);
                } catch (JSONException e) {
                }
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });
        try {
            for(int i = 0; i < NoDb.PICTURE_LIST.size(); i++) {
                String link = pictureList.get(position).getLink();
                if(NoDb.PICTURE_LIST.get(i).getRecipe().getId() == recipe.getId()) {
                    if (!NoDb.PICTURE_LIST.get(i).getLink().isEmpty()) {
                        Picasso.with(context).load(NoDb.PICTURE_LIST.get(i).getLink()).resize(512, 512).into(((RecipeHolder) holder).imageView);
                    }
                    break;
                }
            }
        }
        catch (IndexOutOfBoundsException e){}

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(recipe, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() { return NoDb.RECIPE_LIST.size(); }

    private class RecipeHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView
                authorNameText;
        private final TextView nameText;
        private final TextView timeText;
        private final TextView kcalText;
        private final TextView likesText;


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
