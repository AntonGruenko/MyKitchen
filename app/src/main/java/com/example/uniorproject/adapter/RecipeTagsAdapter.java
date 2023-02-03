package com.example.uniorproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.fragment.FeedFragment;
import com.example.uniorproject.fragment.SetRecipeTagsFragment;
import com.example.uniorproject.noDb.NoDb;

import java.util.List;

public class RecipeTagsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<String> tagList;
    private final SelectedTagsAdapter selectedTagsAdapter;

    public RecipeTagsAdapter(Context context, List<String> tagList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.tagList = tagList;
        selectedTagsAdapter = new SelectedTagsAdapter(context, NoDb.SELECTED_TAG_LIST);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tag_item, parent, false);
        return new RecipeTagsAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RecipeTagsAdapterHolder)holder).tagButton.setText(
                tagList.get(position));

        ((RecipeTagsAdapterHolder)holder).tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NoDb.SELECTED_TAG_LIST.contains(
                        NoDb.TAG_LIST.get(holder.getAdapterPosition())
                )){
                    RecipeTagsAdapter adapter = new RecipeTagsAdapter(context, NoDb.TAG_LIST);
                    NoDb.SELECTED_TAG_LIST.add(
                            NoDb.TAG_LIST.get(holder.getAdapterPosition())
                    );
                    SetRecipeTagsFragment fragment =((SetRecipeTagsFragment)
                            ((MainActivity)context).getSupportFragmentManager().findFragmentByTag("setTags"));
                    fragment.updateAdapter();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    private class RecipeTagsAdapterHolder extends RecyclerView.ViewHolder {

        private final Button tagButton;
        public RecipeTagsAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tagButton = itemView.findViewById(R.id.tag_button);
        }
    }

}
