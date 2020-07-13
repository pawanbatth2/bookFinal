package com.example.book.Viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book.Interface.ItemClickListner;
import com.example.book.R;

import java.text.CollationElementIterator;

public class bookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener { ;
    public static TextView txtBookTitle;
    public static TextView txtBookDetail;
    public static TextView txtBookPrice;
    public ImageView imageView;
    public ItemClickListner listner;
    public bookViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView  = (ImageView) itemView.findViewById(R.id.book_image);
        txtBookTitle = (TextView) itemView.findViewById(R.id.book_name);
        txtBookDetail = (TextView) itemView.findViewById(R.id.book_detail);
        txtBookPrice= (TextView) itemView.findViewById(R.id.book_price);

    }
    public void setItemClickListner(ItemClickListner listner){
        this.listner = listner;
    }
    public void onClick(View view){
        listner.onClick(view, getAdapterPosition(), false);
    }
}
