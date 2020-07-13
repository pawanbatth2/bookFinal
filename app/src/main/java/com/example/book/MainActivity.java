package com.example.book;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book.Models.Users;
import com.example.book.prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button joinNowButton, signInButton;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        joinNowButton = (Button) findViewById(R.id.Join_Now_btn);
        signInButton = (Button) findViewById(R.id.login_btn);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Screen1.class);
                startActivity(intent);
            }
        });
        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Screen3.class);
                startActivity(intent);
            }
        });
        String UserEmailKey = Paper.book().read(prevalent.UserEmailKey);
        String UserPasswordKey = Paper.book().read(prevalent.UserPasswordKey);

        if (UserEmailKey!= "" && UserPasswordKey != ""){
            if(!TextUtils.isEmpty(UserEmailKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserEmailKey, UserPasswordKey); loadingBar.setTitle("signInButton");

                loadingBar.setTitle("Already logged in");
                loadingBar.setMessage("please wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }
    }

    private void AllowAccess(final String email, final String password) {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.child("Users").child(email).exists()){
                    Users usersData = datasnapshot.child("Users").child(email).getValue(Users.class);
                    if (usersData.getEmail().equals(email)){
                        if (usersData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "logged in Successfully", Toast.LENGTH_SHORT).show(); loadingBar.dismiss();
                            loadingBar.dismiss();
                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                            prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Account with this" + email + "do not exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}





