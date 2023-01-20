package com.example.uniorproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.ArrayList;
@Database(entities = {Product.class}, version = 1)
public abstract class ShoppingListDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    private static ShoppingListDatabase INSTANCE;

    public static ShoppingListDatabase getInstance(Context context){

        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ShoppingListDatabase.class, "shoppingList").
                    allowMainThreadQueries().
                    build();
        }

        return INSTANCE;
    }


























    /* static String DATABASE_NAME = "database.db";
    private static int DATABASE_VERSION = 1;
    private static String TABLE_NAME = "shopping_list";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRODUCT = "product";

    private static final int COLUMN_ID_NUM = 0;
    private static final int COLUMN_PRODUCT_NUM = 1;

    private final SQLiteDatabase database;
    private OpenHelper helper;

    public ShoppingListDatabase(Context context) {
        OpenHelper helper = new OpenHelper(context);
        database = helper.getWritableDatabase();
    }

    public long insert(String product) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCT, product);
        return database.insert(TABLE_NAME, null, cv);
    }

    public int update(Product product){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_PRODUCT, product.getTitle());
        return database.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[]{String.valueOf(product.getId())});
    }

    public Product select(long id){
        Cursor c = database.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        Product product = new Product(0,  "");
        c.moveToFirst();
        if(c != null && c.moveToFirst()) {
            product.setId(c.getInt(COLUMN_ID_NUM));
            product.setTitle(c.getString(COLUMN_PRODUCT_NUM));
            c.close();
        }
        return product;
    }

    public long length(){
        long taskCount = DatabaseUtils.queryNumEntries(database, TABLE_NAME);
        return taskCount;
    }

    public ArrayList<Product> selectAll(){
        Cursor c = database.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Product> arr = new ArrayList<>();
        c.moveToFirst();
        if(!c.isAfterLast()){
            do {
                long id = c.getLong(COLUMN_ID_NUM);
                String title = c.getString(COLUMN_PRODUCT_NUM);
                arr.add(new Product(id, title));
            }while (c.moveToNext());
        }
        c.close();
        return arr;
    }

    public void delete(long id){
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE "+COLUMN_ID + "='"+id +"'");
    }

    public void deleteAll(){
        database.delete(TABLE_NAME, null, null);
    }

    public static class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    "id INTEGER primary key AUTOINCREMENT," +
                    COLUMN_PRODUCT + " INT);";
            sqLiteDatabase.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }*/
}
