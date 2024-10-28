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
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.domain.RecipeComment;
import com.example.uniorproject.fragment.AnotherProfileFragment;
import com.example.uniorproject.noDb.NoDb;


import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private final Context context;
    private final List<RecipeComment> recipeComments;
    private final LayoutInflater inflater;

    public CommentAdapter(Context context, List<RecipeComment> recipeComments) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.recipeComments = recipeComments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        RecipeComment recipeComment = recipeComments.get(holder.getAdapterPosition());
        if(!recipeComment.getAuthor().getProfilePic().isEmpty()) {
            Glide.with(context).
                    load(recipeComment.getAuthor().
                            getProfilePic()).
                    centerCrop().
                    into(holder.authorProfilePic);
        }
        else{
            holder.authorProfilePic.setImageResource(R.drawable.ic_baseline_person_24);
        }


        holder.commentContentText.setText(recipeComment.getText());
        holder.authorNameText.setText(recipeComment.getAuthor().getName());

        holder.authorNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AnotherProfileFragment(recipeComment.getAuthor().getId(), recipeComment.getRecipe().getId()), "AnotherProfileFragment")
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeComments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        private final ImageView authorProfilePic;
        private final TextView authorNameText;
        private final TextView commentContentText;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.authorProfilePic = itemView.findViewById(R.id.comment_author_profile_pic);
            this.authorNameText = itemView.findViewById(R.id.comment_author);
            this.commentContentText = itemView.findViewById(R.id.comment_content);
        }
    }
}
