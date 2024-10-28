package com.example.uniorproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeCreatorAdapter;
import com.example.uniorproject.database.ProductsDatabaseHelper;
import com.example.uniorproject.databinding.FragmentCreateRecipeIngredientsBinding;
import com.example.uniorproject.databinding.FragmentFeedBinding;
import com.example.uniorproject.noDb.NoDb;

import java.io.IOException;
import java.util.ArrayList;

public class CreateRecipeIngredientsFragment extends Fragment {

    private RecipeCreatorAdapter recipeCreatorAdapter;
    private Context context;
    private ProductsDatabaseHelper helper;
    private SQLiteDatabase productsDb;
    private Cursor cursor;
    private Cursor menuCursor;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentCreateRecipeIngredientsBinding binding = FragmentCreateRecipeIngredientsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();
        sharedPreferences = context.getSharedPreferences("recipeSharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        helper = new ProductsDatabaseHelper(context);
        if(NoDb.INGREDIENTS_LIST.size() == 0) {
            NoDb.INGREDIENTS_LIST.add("");
            NoDb.INGREDIENTS_DATABASE_LIST.add(new Pair<>(0, 0f));
        }

        try {
            helper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        productsDb = helper.getWritableDatabase();

        cursor = productsDb.rawQuery("SELECT * FROM products ORDER BY productTitle", null);
        cursor.moveToFirst();

        ArrayList<String> labels = new ArrayList<>();
        for(int i = 0; i < 95; i++){
            labels.add(cursor.getString(1));
            cursor.moveToNext();
            if(cursor.isAfterLast())
                break;
        }
        cursor.close();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.array_adapter_item, labels);
        binding.autocompleteTextView.setAdapter(adapter);

        binding.autocompleteTextView.setOnItemClickListener((parent, itemSelected, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            int num = labels.indexOf(item);
            float coeff;
            menuCursor = productsDb.rawQuery("SELECT * FROM products ORDER BY productTitle", null);
            menuCursor.moveToPosition(num);
            try {
                coeff = Integer.parseInt(binding.editCoefficient
                        .getText()
                        .toString()) * 0.01f;
            }catch (Exception e1){
                coeff = 1f;
            }
            editor.putFloat("recipeKcal", sharedPreferences.getFloat("recipeKcal", 0f) + menuCursor.getInt(2) * coeff);
            editor.putFloat("recipeProteins", sharedPreferences.getFloat("recipeProteins", 0f) + menuCursor.getInt(3) * coeff);
            editor.putFloat("recipeFats", sharedPreferences.getFloat("recipeFats", 0f) + menuCursor.getInt(4) * coeff);
            editor.putFloat("recipeCarbohydrates", sharedPreferences.getFloat("recipeCarbohydrates", 0f) + menuCursor.getInt(5) * coeff);
            if(num == 72){
                editor.putFloat("recipeSugar", sharedPreferences.getFloat("recipeSugar", 0f) + coeff * 100);
            }
            editor.commit();
            String ingredient = menuCursor.getString(1) + ", " + (int) coeff * 100 + " Грамм";
            NoDb.INGREDIENTS_DATABASE_LIST.add(new Pair<>(num, coeff));
            NoDb.INGREDIENTS_LIST.add(ingredient);
            recipeCreatorAdapter.notifyDataSetChanged();
            menuCursor.close();
        });

        recipeCreatorAdapter = new RecipeCreatorAdapter(context, NoDb.INGREDIENTS_LIST, 1);
        binding.ingredientsRv.setLayoutManager(new LinearLayoutManager(context));
        binding.ingredientsRv.setAdapter(recipeCreatorAdapter);

        binding.ingredientsRv.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < NoDb.INGREDIENTS_LIST.size(); i++){
                    if (NoDb.INGREDIENTS_LIST.get(i).equals("")){
                        NoDb.INGREDIENTS_LIST.remove(i);
                    }
                }
                NoDb.INGREDIENTS_DATABASE_LIST.add(new Pair<>(0, 0f));
                NoDb.INGREDIENTS_LIST.add("");
                recipeCreatorAdapter.notifyItemInserted(NoDb.INGREDIENTS_LIST.size());
            }
        });

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(NoDb.INGREDIENTS_LIST.size() > 0 && !(NoDb.INGREDIENTS_LIST.size() == 1 && NoDb.INGREDIENTS_LIST.get(0).equals(""))) {
                    for (int i = 0; i < NoDb.INGREDIENTS_LIST.size(); i++){
                        if (NoDb.INGREDIENTS_LIST.get(i).equals("")){
                            NoDb.INGREDIENTS_LIST.remove(i);
                        }
                    }
                    ((MainActivity) context).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.create_container, new CreateRecipeGuideFragment(), "setGuide")
                            .commit();
                }
                else{
                    Toast.makeText(getContext(), "Введите ингредиенты!", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.create_container, new CreateRecipeFragment(), "createRecipe")
                        .commit();
            }
        });
        return view;
    }
}