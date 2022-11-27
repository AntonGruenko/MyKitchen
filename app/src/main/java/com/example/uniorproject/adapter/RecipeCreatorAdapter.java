package com.example.uniorproject.adapter;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.domain.Recipe;
import com.example.uniorproject.noDb.NoDb;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeCreatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> contentList;
    private int fragmentType;
    private Uri imageUri;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("recipePictures");

    public RecipeCreatorAdapter(Context context, List<String> contentList, int fragmentType) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.contentList = contentList;
        this.fragmentType = fragmentType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.create_recipe_item, parent, false);

        return new RecipeCreatorAdapter.RecipeCreatorHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String content = contentList.get(position);
        int pos = position;
        ((RecipeCreatorHolder)holder).editContent.setText(content);
        if(fragmentType == 1){
            ((RecipeCreatorHolder) holder).editContent.setHint("Ингредиент");
        }
        else {
            ((RecipeCreatorHolder) holder).editContent.setHint("Шаг");
        }
        ((RecipeCreatorHolder)holder).editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(fragmentType == 1) {
                    NoDb.INGREDIENTS_LIST.set(pos, charSequence.toString());
                }
                else{
                    NoDb.GUIDE_LIST.set(pos, charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(fragmentType == 1){

        }
    }

    public List<String> getData(){
        return contentList;
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    private class RecipeCreatorHolder extends RecyclerView.ViewHolder {
        private Button deleteButton;
        private ImageView contentImage;
        private TextInputEditText editContent;
        private RecipeCreatorAdapter recipeCreatorAdapter;

        public RecipeCreatorHolder(@NonNull View itemView) {
            super(itemView);

            deleteButton = itemView.findViewById(R.id.delete_button);
            editContent = itemView.findViewById(R.id.edit_content);
            contentImage = itemView.findViewById(R.id.content_image);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(fragmentType == 1 && NoDb.INGREDIENTS_LIST.size() > 1) {
                        NoDb.INGREDIENTS_LIST.remove(getAdapterPosition());
                        recipeCreatorAdapter.notifyItemRemoved(getAdapterPosition());
                    }
                    else if(fragmentType == 2 && NoDb.GUIDE_LIST.size() > 1){
                        NoDb.GUIDE_LIST.remove(getAdapterPosition());
                        recipeCreatorAdapter.notifyItemRemoved(getAdapterPosition());
                    }
                }

            });

            if(fragmentType == 2){
                contentImage.setVisibility(View.GONE);
            }

        }

        public RecipeCreatorHolder linkAdapter(RecipeCreatorAdapter adapter){
            this.recipeCreatorAdapter = adapter;
            return this;
        }

    }

}
