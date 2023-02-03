package com.example.uniorproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniorproject.R;
import com.example.uniorproject.database.Product;
import com.example.uniorproject.database.ShoppingListDatabase;
import com.example.uniorproject.noDb.NoDb;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ShoppingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Product> products;
    private final ShoppingListHandler handler;

    public ShoppingListAdapter(Context context, ShoppingListHandler handler) {
        this.context = context;
        this.handler = handler;


    }

    public void setProductsList(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.create_recipe_item, parent, false);
        return new ShoppingListAdapter.ShoppingListHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int pos = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.editItem(products.get(pos));
            }
        });

        ((ShoppingListHolder) holder).content.setText(products.get(pos).getTitle());
        ((ShoppingListHolder) holder).deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.deleteItem(products.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(products == null){
            return 0;
        }
        else {
            return products.size();
        }
    }

    private class ShoppingListHolder extends RecyclerView.ViewHolder {
        private final Button deleteButton;
        private final TextView content;
        private final ImageView cameraImage;
        private ShoppingListAdapter shoppingListAdapter;

        public ShoppingListHolder(@NonNull View itemView) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.delete_button);
            content = itemView.findViewById(R.id.content);
            cameraImage = itemView.findViewById(R.id.content_image);
            cameraImage.setVisibility(View.GONE);

        }

        public ShoppingListHolder linkAdapter(ShoppingListAdapter adapter){
            this.shoppingListAdapter = adapter;
            return this;
        }

    }

    public interface ShoppingListHandler{
        void deleteItem(Product product);
        void editItem(Product product);
        void itemClick();
    }

}
