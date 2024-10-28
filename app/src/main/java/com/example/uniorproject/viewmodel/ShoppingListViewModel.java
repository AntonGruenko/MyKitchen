package com.example.uniorproject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.uniorproject.database.Product;
import com.example.uniorproject.database.ShoppingListDatabase;

import java.util.List;

public class ShoppingListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Product>> listOfProducts;
    private final ShoppingListDatabase database;
    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        listOfProducts = new MutableLiveData<>();
        database = ShoppingListDatabase.getInstance(getApplication().getApplicationContext());
    }

    public MutableLiveData<List<Product>> getProductsListObserver(){
        return listOfProducts;
    }

    public void getAllProductList(){
        List<Product> productList = database.productDao().getAllProducts();
        if(productList.size() > 0){
            listOfProducts.postValue(productList);
        }
        else{
            listOfProducts.postValue(null );
        }
    }

    public void insertProduct(String title){
        Product product = new Product();
        product.setTitle(title);
        database.productDao().insertProduct(product);
        getAllProductList();
    }

    public void updateProduct(Product product){
        database.productDao().updateProduct(product);
        getAllProductList();
    }

    public void deleteProduct(Product product){
        database.productDao().deleteProduct(product);
        getAllProductList();
    }
}
