package com.example.book;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.book.Models.Cart;


import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "cartdb";

    private static final String TABLE_CART = "Cart";



    //table favorite
    private static final String KEY_ID = "id";

    private static final String KEY_Name = "name";
    private static final String KEY_Price = "price";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_CART_NAME = "CREATE TABLE " + TABLE_CART + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_Name + " TEXT,"
                + KEY_Price + " TEXT"

                + ")";

        db.execSQL(CREATE_TABLE_CART_NAME);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);



        // Create tables again
        onCreate(db);
    }

    public void AddProduct(String title,String price )
    {
        /// storing data in the table
        SQLiteDatabase db = this.getWritableDatabase(); // getting writable database

        ContentValues values = new ContentValues();
        /// sending values to the table

        values.put(KEY_Name, title);
        values.put(KEY_Price, price);

        // Inserting Row
        db.insert(TABLE_CART, null, values);
        db.close(); // Closing database connection

    }




    public List<Cart> getAllNamesData() {
        List<Cart> dataList = new ArrayList<Cart>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cart data;
                dataList.add(data=new Cart(cursor.getString(1),cursor.getString(2)));

            } while (cursor.moveToNext());
        }

        // return contact list
        return dataList;
    }



    public void DeleteDogName() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, "1",null);
        db.close();
    }

    public enum DatabaseManager {
        INSTANCE;
        private SQLiteDatabase db;
        private boolean isDbClosed = true;
        DatabaseHandler dbHelper;

        public void init(Context context) {
            dbHelper = new DatabaseHandler(context);
            if (isDbClosed) {
                isDbClosed = false;
                this.db = dbHelper.getWritableDatabase();
            }

        }


        public boolean isDatabaseClosed() {
            return isDbClosed;
        }

        public void closeDatabase() {
            if (!isDbClosed && db != null) {
                isDbClosed = true;
                db.close();
                dbHelper.close();
            }
        }
    }
}
