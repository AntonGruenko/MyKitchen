package com.example.uniorproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uniorproject.R;
import com.example.uniorproject.adapter.RecipeCreatorAdapter;
import com.example.uniorproject.adapter.ShoppingListAdapter;
import com.example.uniorproject.database.Product;
import com.example.uniorproject.database.ShoppingListDatabase;
import com.example.uniorproject.databinding.FragmentShoppingListBinding;
import com.example.uniorproject.noDb.NoDb;

public class ShoppingListFragment extends Fragment {

    private FragmentShoppingListBinding binding;
    private ShoppingListAdapter adapter;
    private ShoppingListDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        database = new ShoppingListDatabase(getContext());
        for (int i = 0; i < database.length(); i++) {
            NoDb.PRODUCT_LIST.add(database.select(i+1));
        }

        adapter = new ShoppingListAdapter(getActivity());

        binding.shoppingListRecyclerView.setAdapter(adapter);
        binding.shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoDb.PRODUCT_LIST.add(new Product(NoDb.PRODUCT_LIST.size() + 1, ""));
                adapter.notifyItemInserted(NoDb.PRODUCT_LIST.size());
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        database = new ShoppingListDatabase(getActivity());
        database.deleteAll();
        for (int i = 0; i < NoDb.PRODUCT_LIST.size(); i++) {
            database.insert(i + 1, NoDb.PRODUCT_LIST.get(i).getTitle());
        }
        NoDb.PRODUCT_LIST.clear();
        super.onStop();
    }
}