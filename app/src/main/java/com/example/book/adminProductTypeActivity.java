package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class adminProductTypeActivity extends AppCompatActivity {
    private ImageView adults1, adults2, kids1, kids2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_type);

        adults1 = (ImageView) findViewById(R.id.adults1);
        adults2 = (ImageView) findViewById(R.id.adults2);
        kids1 = (ImageView) findViewById(R.id.kids1);
        kids2= (ImageView) findViewById(R.id.kids2);

        adults1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminProductTypeActivity.this, adminAddNewProductActivity.class);
                intent.putExtra("product", "adults1");
                startActivity(intent);
            }
        });
        adults2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminProductTypeActivity.this, adminAddNewProductActivity.class);
                intent.putExtra("product", "adults2");
                startActivity(intent);
            }
        });
        kids1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminProductTypeActivity.this, adminAddNewProductActivity.class);
                intent.putExtra("product", "kids1");
                startActivity(intent);

            }
        });
        kids2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminProductTypeActivity.this, adminAddNewProductActivity.class);
                intent.putExtra("product", "kids2");
                startActivity(intent);
            }
        });
    }
}
