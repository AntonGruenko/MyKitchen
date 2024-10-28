package com.example.uniorproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.uniorproject.R;
import com.example.uniorproject.domain.Day;
import com.example.uniorproject.domain.Meal;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONObject;

import java.util.List;

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealsHolder> {
    private final Context context;
    private final List<Meal> meals;
    private final LayoutInflater inflater;
    private final MealsCallback callback;
    private Day day;


    public MealsAdapter(Context context, List<Meal> meals, MealsCallback callback, Day day) {
        this.context = context;
        this.meals = meals;
        inflater = LayoutInflater.from(context);
        this.callback = callback;
        this.day = day;
    }

    @NonNull
    @Override
    public MealsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.meal_list_item, parent, false);
        return new MealsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealsHolder holder, int position) {
        Meal meal = meals.get(holder.getAdapterPosition());
        Recipe mealRecipe = meal.getRecipe();
        holder.mealTitle.setText(mealRecipe.getName());
        holder.mealKcal.setText(String.valueOf(mealRecipe.getKcal()));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.getAdapterPosition() != -1) {
                    new VolleyAPI(context).deleteMeal(meal, new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            day = new Day(day.getId(),
                                    day.getUser(),
                                    day.getDay(),
                                    day.getKcal() - meal.getRecipe().getKcal(),
                                    day.getProteins() - meal.getRecipe().getProteins(),
                                    day.getFats() - meal.getRecipe().getFats(),
                                    day.getCarbohydrates() - meal.getRecipe().getCarbohydrates(),
                                    false);
                            new VolleyAPI(context).updateDay(
                                    day.getId(),
                                    day.getUser().getId(),
                                    day.getDay(),
                                    day.getKcal(),
                                    day.getProteins(),
                                    day.getFats(),
                                    day.getCarbohydrates(),
                                    false, new VolleyCallback() {
                                        @Override
                                        public void onSuccess(JSONObject response) {
                                            callback.onCall(response);

                                        }

                                        @Override
                                        public void onError(@Nullable VolleyError error) {

                                        }
                                    }

                            );
                            meals.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                            notifyItemRemoved(holder.getAdapterPosition());
                            notifyItemRangeChanged(holder.getAdapterPosition(), meals.size());
                        }

                        @Override
                        public void onError(@Nullable VolleyError error) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    public class MealsHolder extends RecyclerView.ViewHolder {

        private final TextView mealTitle;
        private final Button deleteButton;
        private final TextView mealKcal;

        public MealsHolder(@NonNull View itemView) {
            super(itemView);
            mealTitle = itemView.findViewById(R.id.meal_title);
            deleteButton = itemView.findViewById(R.id.meal_delete_button);
            mealKcal = itemView.findViewById(R.id.meal_kcal);
        }
    }

    public interface MealsCallback{
        void onCall(JSONObject response);
    }
}
