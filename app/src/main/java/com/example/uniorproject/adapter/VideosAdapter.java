package com.example.uniorproject.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.domain.Post;
import com.example.uniorproject.domain.PostLike;
import com.example.uniorproject.domain.User;
import com.example.uniorproject.domain.mapper.UserMapper;
import com.example.uniorproject.fragment.AnotherProfileFragment;
import com.example.uniorproject.rest.VolleyAPI;
import com.example.uniorproject.rest.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private final Context context;
    private final List<Post> posts;
    private final LayoutInflater inflater;
    private SharedPreferences sharedPreferences;
    private User currentUser;

    public VideosAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
        this.sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoViewHolder(inflater.inflate(R.layout.view_pager_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.setVideoData(posts.get(holder.getAdapterPosition()));


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    class VideoViewHolder extends RecyclerView.ViewHolder{
        private final VideoView videoView;
        private final TextView videoTitle;
        private final TextView authorName;
        private final ProgressBar progressBar;
        private final CheckBox likesCheckBox;


        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            authorName = itemView.findViewById(R.id.videoAuthor);
            progressBar = itemView.findViewById(R.id.videoProgressBar);
            likesCheckBox = itemView.findViewById(R.id.likes_check_box);
        }

        void setVideoData(Post post){
            videoTitle.setText(post.getText());
            authorName.setText(post.getAuthor().getName());
            videoView.setVideoPath(post.getPicture());

            authorName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new AnotherProfileFragment(post.getAuthor().getId(), AnotherProfileFragment.FROM_POST))
                            .commit();
                }
            });

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    progressBar.setVisibility(View.GONE);
                    mediaPlayer.start();

                    float videoRatio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
                    float screenRatio = videoView.getWidth() / (float) videoView.getHeight();

                    float scale = videoRatio / screenRatio;

                    if(scale >= 1f){
                        videoView.setScaleX(scale);
                    }
                    else{
                        videoView.setScaleY(1f / scale);
                    }
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(videoView.isPlaying()){
                        videoView.pause();
                    }
                    else{
                        videoView.start();
                    }
                }
            });

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
                                    likesCheckBox.setChecked(true);
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
                        likesCheckBox.setText(num);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(@Nullable VolleyError error) {

                }
            });

            likesCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    VolleyCallback deleteCallback = new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            likesCheckBox.setClickable(true);
                            likesCheckBox.setChecked(false);
                            try {
                                likesCheckBox.setText(response.getString("num"));
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
                            likesCheckBox.setClickable(true);
                            likesCheckBox.setChecked(true);
                            try {
                                likesCheckBox.setText(String.valueOf(response.getInt("num")));
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
                            likesCheckBox.setText(String.valueOf(0));
                            likesCheckBox.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    likesCheckBox.setClickable(false);
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
                    likesCheckBox.setClickable(false);
                    new VolleyAPI(context).findUserByEmail(sharedPreferences.getString("userEmail", ""), emailCallback);
                }
            });
        }
    }
}
