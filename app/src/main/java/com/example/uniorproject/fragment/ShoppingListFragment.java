package com.example.uniorproject.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.uniorproject.MainActivity;
import com.example.uniorproject.R;
import com.example.uniorproject.adapter.ShoppingListAdapter;
import com.example.uniorproject.database.Product;
import com.example.uniorproject.database.ShoppingListDatabase;
import com.example.uniorproject.databinding.FragmentShoppingListBinding;
import com.example.uniorproject.viewmodel.ShoppingListViewModel;

import java.util.List;

public class ShoppingListFragment extends Fragment implements ShoppingListAdapter.ShoppingListHandler {

    private FragmentShoppingListBinding binding;
    private ShoppingListAdapter adapter;
    private ShoppingListDatabase database;
    private ShoppingListViewModel viewModel;
    private Product newProduct;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = getContext();

        initViewModel();
        initRecyclerView();
        viewModel.getAllProductList();

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();

            }
        });

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCategoryDialog(false);
            }
        });

        return view;
    }

    private void showAddCategoryDialog(boolean isForEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(context).create();
        View dialogView = getLayoutInflater().inflate( R.layout.insert_item_layout, null);
        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = enterCategoryInput.getText().toString();

                if(isForEdit){
                    newProduct.setTitle(title);
                    viewModel.updateProduct(newProduct);
                } else {
                    viewModel.insertProduct(title);
                }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    private void initRecyclerView(){
        adapter = new ShoppingListAdapter(getActivity(), ShoppingListFragment.this);
        binding.shoppingListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.shoppingListRecyclerView.setAdapter(adapter);
    }

    private void initViewModel(){
        viewModel = new ViewModelProvider(((MainActivity) context)).get(ShoppingListViewModel.class);
        viewModel.getProductsListObserver().observe(((MainActivity) context), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                try {
                    adapter.setProductsList(products);
                }
                catch (NullPointerException e){

                }
            }
        });
    }

    @Override
    public void deleteItem(Product product) {
        viewModel.deleteProduct(product);
    }

    @Override
    public void editItem(Product product) {
        this.newProduct = product;
        showAddCategoryDialog(true);

    }

    @Override
    public void itemClick() {

    }
}