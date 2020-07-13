package com.example.book;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.book.Models.Books;
import com.example.book.Models.Cart;
import com.squareup.picasso.Picasso;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
private Context context;
    static DatabaseHandler db;
    private DatabaseHandler.DatabaseManager dbManager;
private List<Cart> items;




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

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.items = cartList;

    }

    @Override
    public  MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_layout, parent, false);

        return new  MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder( MyViewHolder holder, final int position) {
        final Cart productItems = items.get(position);
        holder.txtBookTitle.setText(productItems.getId());

        holder.txtBookPrice.setText("Price = " +productItems.getName() + "$");



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

