package com.example.book;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.book.Models.Books;
import com.example.book.Viewholder.bookViewHolder;
import com.example.book.prevalent.prevalent;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomePage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
private DatabaseReference Productref;
RecyclerView recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Productref = FirebaseDatabase.getInstance().getReference().child("Books");
        Paper.init(this);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("HomePage");
//        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        CircleImageView profileImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText((prevalent.currentOnlineUser.getEmail()));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cart, R.id.nav_orders, R.id.nav_setting, R.id.nav_Categories, R.id.nav_quit)
                .setDrawerLayout(drawer)
                .build();
        Paper.book().destroy();
        Intent intent = new Intent(HomePage.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Books>options = new FirebaseRecyclerOptions.Builder<Books>()
                .setQuery(Productref, Books.class)
                .build();

        FirebaseRecyclerAdapter<Books, bookViewHolder>adapter =
                new FirebaseRecyclerAdapter<Books, bookViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull bookViewHolder holder, int position, @NonNull Books models) {
                        bookViewHolder.txtBookTitle.setText(models.getbTitle());
                        bookViewHolder.txtBookDetail.setText(models.getDetail());
                        bookViewHolder.txtBookPrice.setText("Price = " +models.getPrice() + "$");
                        Picasso.get().load(models.getImage()).into(holder.imageView);
                    }

                    @NonNull
                    @Override
                    public bookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
                        bookViewHolder holder = new bookViewHolder(view);
                        return holder;
                    }
                };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void onClick(View view){
        Intent  intent = new Intent(HomePage.this,SettingActivity.class);
        startActivity(intent);

    }
}
