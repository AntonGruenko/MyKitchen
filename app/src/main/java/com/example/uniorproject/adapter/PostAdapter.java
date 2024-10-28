package com.example.uniorproject.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.PostLike;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.domain.RecipeLike;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.fragment.AnotherProfileFragment;
import com.example.uniorproject.noDb.NoDb;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Post> postList;
    private final SharedPreferences sharedPreferences;
    private User currentUser;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.postList = postList;
        this.sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
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
        holder.authorName.setText(post.getAuthor().getName());
        holder.likesCheckBox.setText(String.valueOf(0));
        holder.contentText.setText(String.valueOf(post.getText()));

        holder.authorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new AnotherProfileFragment(post.getAuthor().getId(), AnotherProfileFragment.FROM_POST))
                        .commit();
            }
        });
        if(!post.getPicture().isEmpty()) {
            Glide.with(context)
                    .load(post.getPicture())
                    .apply(new RequestOptions().override(256, 256))
                    .centerCrop()
                    .into(holder.itemImage);
        }

        new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                currentUser = UserMapper.userFromJson(response);
                new VolleyAPI(context).checkPostLikeByUser(post.getId(), currentUser.getId(), new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            boolean res = response.getBoolean("res");
                            if (res) {
                                holder.likesCheckBox.setChecked(true);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                });
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });

        new VolleyAPI(context).findPostLikesByPost(post.getId(), new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String num = response.getString("num");
                    holder.likesCheckBox.setText(num);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@Nullable VolleyError error) {

            }
        });

        holder.likesCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VolleyCallback deleteCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        holder.likesCheckBox.setClickable(true);
                        holder.likesCheckBox.setChecked(false);
                        try {
                            holder.likesCheckBox.setText(response.getString("num"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                };

                VolleyCallback addCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        holder.likesCheckBox.setClickable(true);
                        holder.likesCheckBox.setChecked(true);
                        try {
                            holder.likesCheckBox.setText(String.valueOf(response.getInt("num")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                };

                VolleyCallback checkCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            boolean res = response.getBoolean("res");
                            if(res){
                                new VolleyAPI(context).deletePostLike(post, currentUser, deleteCallback);
                            }
                            else {
                                new VolleyAPI(context).addPostLike(new PostLike(post, currentUser), addCallback);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {
                        holder.likesCheckBox.setText(String.valueOf(0));
                        holder.likesCheckBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                holder.likesCheckBox.setClickable(false);
                                new VolleyAPI(context).checkPostLikeByUser(post.getId(), currentUser.getId(), new VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        try {
                                            boolean res = response.getBoolean("res");
                                            if(res){
                                                new VolleyAPI(context).deletePostLike(post, currentUser, deleteCallback);
                                            }
                                            else {
                                                new VolleyAPI(context).addPostLike(new PostLike(post, currentUser), addCallback);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(@Nullable VolleyError error) {

                                    }
                                });

                            }
                        });
                    }
                };

                VolleyCallback emailCallback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        currentUser = UserMapper.userFromJson(response);
                        new VolleyAPI(context).checkPostLikeByUser(post.getId(), currentUser.getId(), checkCallback);
                    }

                    @Override
                    public void onError(@Nullable VolleyError error) {

                    }
                };
                holder.likesCheckBox.setClickable(false);
                new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), emailCallback);
            }
        });


    }
    @Override
    public int getItemCount() {
        return NoDb.POST_LIST.size();
    }

    class PostHolder extends RecyclerView.ViewHolder {
        private final ImageView itemImage;
        private final TextView authorName;
        private final CheckBox likesCheckBox;
        private final TextView contentText;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_picture);
            authorName = itemView.findViewById(R.id.item_author_name);
            likesCheckBox = itemView.findViewById(R.id.item_likes);
            contentText = itemView.findViewById(R.id.item_text);
        }
    }
}
