package com.example.book;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.book.Models.Books;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Book;


public class BooksAdapter  extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {
private Context context;
    static DatabaseHandler db;
    private DatabaseHandler.DatabaseManager dbManager;
private List<Books> items;




public class MyViewHolder extends RecyclerView.ViewHolder {
    public   TextView txtBookTitle;
    public   TextView txtBookDetail;
    public   TextView txtBookPrice;
    public ImageView imageView;
    public MyViewHolder(View view) {
        super(view);
        imageView  = (ImageView) itemView.findViewById(R.id.book_image);
        txtBookTitle = (TextView) itemView.findViewById(R.id.book_name);
        txtBookDetail = (TextView) itemView.findViewById(R.id.book_detail);
        txtBookPrice= (TextView) itemView.findViewById(R.id.book_price);    }
}
//    public HabitTestAdapter(List<HabitModel> itemsNew, OnItemClickListener listener) {
//        this.itemsNew = itemsNew;
//        this.listener = listener;
//    }

    public BooksAdapter(Context context, List<Books> cartList) {
        this.context = context;
        this.items = cartList;

    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_layout, parent, false);

        return new  MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder( MyViewHolder holder, final int position) {
        final Books productItems = items.get(position);
        holder.txtBookTitle.setText(productItems.getbTitle());
        holder.txtBookDetail.setText(productItems.getDetail());
        holder.txtBookPrice.setText("Price = " +productItems.getPrice() + "$");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DatabaseHandler(context);
                dbManager = DatabaseHandler.DatabaseManager.INSTANCE;  /// instance of database
                dbManager.init(context);
                db.AddProduct(productItems.getbTitle(),productItems.getPrice());
                Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();

            }
        });

      Picasso.get().load(productItems.getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

