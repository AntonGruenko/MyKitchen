package com.example.uniorproject.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniorproject.R;
import com.example.uniorproject.database.Product;
import com.example.uniorproject.database.ShoppingListDatabase;
import com.example.uniorproject.noDb.NoDb;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;

    public ShoppingListAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.create_recipe_item, parent, false);
        return new ShoppingListAdapter.ShoppingListHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String content = NoDb.PRODUCT_LIST.get(position).getTitle();
        int pos = position;
        ((ShoppingListHolder)holder).editContent.setText(content);
            ((ShoppingListHolder) holder).editContent.setHint("Продукт");

        ((ShoppingListHolder)holder).editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    NoDb.PRODUCT_LIST.set(pos, new Product(pos, charSequence.toString()));
                }
                catch (IndexOutOfBoundsException exception){
                    Log.d("Exception", exception.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((ShoppingListHolder) holder).deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NoDb.PRODUCT_LIST.size() > 1 && holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                    NoDb.PRODUCT_LIST.remove(holder.getAdapterPosition());
                    ShoppingListAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return NoDb.PRODUCT_LIST.size();
    }

    private class ShoppingListHolder extends RecyclerView.ViewHolder {
        private Button deleteButton;
        private TextInputEditText editContent;
        private ShoppingListAdapter shoppingListAdapter;

        public ShoppingListHolder(@NonNull View itemView) {
            super(itemView);

            deleteButton = itemView.findViewById(R.id.delete_button);
            editContent = itemView.findViewById(R.id.edit_content);

        }

        public ShoppingListHolder linkAdapter(ShoppingListAdapter adapter){
            this.shoppingListAdapter = adapter;
            return this;
        }

    }
}
