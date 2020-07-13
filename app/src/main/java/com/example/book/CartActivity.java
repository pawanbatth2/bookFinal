package com.example.book;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.book.Models.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    static DatabaseHandler db;
    private DatabaseHandler.DatabaseManager dbManager;
    List<Cart> arrayList=new ArrayList<>();
    RecyclerView recycle;
    CartAdapter cartAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recycle=findViewById(R.id.recycle);
        recycle.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        db = new DatabaseHandler(getApplicationContext());
        dbManager = DatabaseHandler.DatabaseManager.INSTANCE;  /// instance of database
        dbManager.init(getApplicationContext());
        arrayList=db.getAllNamesData();
        cartAdapter=new CartAdapter(this,arrayList);
        recycle.setAdapter(cartAdapter);


    }
}
