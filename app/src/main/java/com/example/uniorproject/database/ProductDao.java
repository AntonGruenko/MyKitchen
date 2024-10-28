package com.example.uniorproject.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<Product> getAllProducts();

    @Insert
    void insertProduct(Product... product);

    @Update
    void updateProduct(Product... product);

    @Delete
    void deleteProduct(Product product);



}
