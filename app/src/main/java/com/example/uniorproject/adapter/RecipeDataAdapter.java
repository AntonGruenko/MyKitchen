package com.example.uniorproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniorproject.R;

import java.util.List;

public class RecipeDataAdapter extends RecyclerView.Adapter<RecipeDataAdapter.RecipeDataHolder> {
    private Context context;
    private List<String> elements;
    private LayoutInflater inflater;

    public RecipeDataAdapter(Context context, List<String> elements) {
        this.context = context;
        this.elements = elements;
        inflater = LayoutInflater.from(context);
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
                holder.getAdapterPosition()+1, elements.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    class RecipeDataHolder extends RecyclerView.ViewHolder{

        TextView elementText;

        public RecipeDataHolder(@NonNull View itemView) {
            super(itemView);
            elementText = itemView.findViewById(R.id.item_data);
        }
    }
}
