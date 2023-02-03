package com.example.uniorproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.database.ProductsDatabaseHelper;
import com.example.uniorproject.noDb.NoDb;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeCreatorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<String> contentList;
    private final int fragmentType;
    private Uri imageUri;
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference("recipePictures");

    public RecipeCreatorAdapter(Context context, List<String> contentList, int fragmentType) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.contentList = contentList;
        this.fragmentType = fragmentType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.ingredients_and_guides_item, parent, false);

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
            try {
                Picasso.with(context).load(NoDb.PICTURE_LINK_LIST.get(pos)).placeholder(R.drawable.ic_baseline_camera_alt_24).into(((RecipeCreatorHolder) holder).contentImage);
            }
            catch (IllegalArgumentException e){

            }

        }
        ((RecipeCreatorHolder)holder).editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (fragmentType == 1) {
                        NoDb.INGREDIENTS_LIST.set(holder.getAdapterPosition(), charSequence.toString());
                    } else {
                        NoDb.GUIDE_LIST.set(holder.getAdapterPosition(), charSequence.toString());
                    }
                }
                catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(fragmentType == 2){
            ((RecipeCreatorHolder) holder).contentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openFileChooser(pos);
                }
            });
        }
    }

    public List<String> getData(){
        return contentList;
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public class RecipeCreatorHolder extends RecyclerView.ViewHolder {
        private final Button deleteButton;
        private final AppCompatImageView contentImage;
        private final TextView editContent;
        private RecipeCreatorAdapter recipeCreatorAdapter;
        private ProductsDatabaseHelper helper;
        private SQLiteDatabase productsDb;
        private Cursor cursor;
        public RecipeCreatorHolder(@NonNull View itemView) {
            super(itemView);

            deleteButton = itemView.findViewById(R.id.delete_button);
            editContent = itemView.findViewById(R.id.edit_content);
            contentImage = itemView.findViewById(R.id.content_image);

            helper = new ProductsDatabaseHelper(context);
            if(fragmentType == 1){
                try {
                    helper.updateDataBase();
                } catch (IOException mIOException) {
                    throw new Error("UnableToUpdateDatabase");
                }

                productsDb = helper.getWritableDatabase();

            }
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(fragmentType == 1 && NoDb.INGREDIENTS_LIST.size() > 1 && position != -1) {
                        NoDb.INGREDIENTS_LIST.remove(position);
                        recipeCreatorAdapter.notifyItemRemoved(position);
                        SharedPreferences sharedPreferences = context.getSharedPreferences("recipeSharedPreferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        cursor = productsDb.rawQuery("SELECT * FROM products ORDER BY productTitle", null);
                        cursor.moveToFirst();

                        int id = NoDb.INGREDIENTS_DATABASE_LIST.get(position).first;
                        float coeff = NoDb.INGREDIENTS_DATABASE_LIST.get(position).second;
                        if(id != 0) {
                            cursor.moveToPosition(id);
                            editor.putFloat("recipeKcal", sharedPreferences.getFloat("recipeKcal", 0f) - cursor.getFloat(2) * coeff);
                            editor.putFloat("recipeProteins", sharedPreferences.getFloat("recipeProteins", 0f) - cursor.getFloat(3) * coeff);
                            editor.putFloat("recipeFats", sharedPreferences.getFloat("recipeFats", 0f) - cursor.getFloat(4) * coeff);
                            editor.putFloat("recipeCarbohydrates", sharedPreferences.getFloat("recipeCarbohydrates", 0f) - cursor.getFloat(5) * coeff);
                            if(id == 72){
                                editor.putFloat("recipeSugar", sharedPreferences.getFloat("recipeSugar", 0f) - coeff * 100);
                            }
                            editor.apply();
                        }
                        NoDb.INGREDIENTS_DATABASE_LIST.remove(position);
                        cursor.close();
                    }
                    else if(fragmentType == 2 && NoDb.GUIDE_LIST.size() > 1 && position != -1){
                        NoDb.GUIDE_LIST.remove(position);
                        Picasso.with(context).load(R.drawable.ic_baseline_camera_alt_24).placeholder(R.drawable.ic_baseline_camera_alt_24).fit().into(contentImage);
                        NoDb.PICTURE_LINK_LIST.remove(position);
                        recipeCreatorAdapter.notifyItemRemoved(position);
                    }
                }

            });

            if(fragmentType == 1){
                contentImage.setVisibility(View.GONE);
            }
            else {
                Picasso.with(context).load(R.drawable.ic_baseline_camera_alt_24).placeholder(R.drawable.ic_baseline_camera_alt_24).fit().into(contentImage);
            }

        }

        public RecipeCreatorHolder linkAdapter(RecipeCreatorAdapter adapter){
            this.recipeCreatorAdapter = adapter;
            return this;
        }

    }

    private void openFileChooser(int position){

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("position", position);
        editor.apply();
        ((MainActivity)context).startActivityForResult(intent, 7);

    }

}
