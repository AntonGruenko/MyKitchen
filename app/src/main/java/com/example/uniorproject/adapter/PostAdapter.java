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
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.noDb.NoDb;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post post = NoDb.POST_LIST.get(position);
        ((PostHolder) holder).authorName.setText(post.getAuthor().getName());
        ((PostHolder) holder).likesText.setText(String.valueOf(post.getLikes()));
        ((PostHolder) holder).contentText.setText(String.valueOf(post.getText()));
        Picasso.with(context)
                .load(post.getPicture())
                .into(((PostHolder) holder).itemImage, new Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        ((PostHolder) holder).itemImage.setVisibility(View.GONE);
                    }
                });

    }
    @Override
    public int getItemCount() {
        return NoDb.POST_LIST.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView authorName;
        private TextView likesText;
        private TextView contentText;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_picture);
            authorName = itemView.findViewById(R.id.item_author_name);
            likesText = itemView.findViewById(R.id.item_likes);
            contentText = itemView.findViewById(R.id.item_text);
        }
    }
}
