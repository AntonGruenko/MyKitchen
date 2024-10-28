package com.example.uniorproject.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uniorproject.R;
import com.example.uniorproject.noDb.NoDb;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class RecipeSearchAdapter extends RecyclerView.Adapter<RecipeSearchAdapter.RecipeSearchViewHolder> {
    public static final int INGREDIENTS_SEARCH = 1;
    public static final int TAGS_SEARCH = 2;
    private final Context context;
    private final List<RecipeSearchText> elements;
    private final LayoutInflater inflater;
    private final int elementType;

    public RecipeSearchAdapter(Context context, List<RecipeSearchText> elements, int elementType) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.elements = elements;
        this.elementType = elementType;
    }

    @NonNull
    @Override
    public RecipeSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recipe_search_item, parent, false);
        return new RecipeSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeSearchViewHolder holder, int position) {
        if(elementType == INGREDIENTS_SEARCH){
            holder.textField.setHint("Ингредиент");
        }
        else if(elementType == TAGS_SEARCH){
            holder.textField.setHint("Тег");
        }

        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RecipeSearchText newText = new RecipeSearchText(charSequence.toString(),
                        elements.get(holder.getAdapterPosition()).isContains());
                elements.set(holder.getAdapterPosition(),newText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.searchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                RecipeSearchText newText = new RecipeSearchText(elements.get(holder.getAdapterPosition()).getQuery(), b);
                elements.set(holder.getAdapterPosition(), newText);
            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public class RecipeSearchViewHolder extends RecyclerView.ViewHolder {
        private final Button deleteButton;
        private final SwitchCompat searchSwitch;
        private final TextInputEditText editText;
        private final TextInputLayout textField;
        public RecipeSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            deleteButton = itemView.findViewById(R.id.delete_button);
            searchSwitch = itemView.findViewById(R.id.search_item_switch);
            editText = itemView.findViewById(R.id.edit_content);
            textField = itemView.findViewById(R.id.filledTextField_Title);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(elementType == INGREDIENTS_SEARCH && getAdapterPosition() != -1){
                        editText.setText("");
                        NoDb.SEARCH_INGREDIENTS_LIST.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                    else if(elementType == TAGS_SEARCH && getAdapterPosition() != -1){
                        editText.setText("");
                        NoDb.SEARCH_TAGS_LIST.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                }
            });
        }
    }

    public static class RecipeSearchText{
        private String query;
        private boolean contains;

        public RecipeSearchText(String query, boolean contains) {
            this.query = query;
            this.contains = contains;
        }

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public boolean isContains() {
            return contains;
        }

        public void setContains(boolean contains) {
            this.contains = contains;
        }
    }
}
