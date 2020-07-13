
package com.example.book;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.book.Models.Users;
import com.example.book.prevalent.prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Screen1 extends AppCompatActivity {
    private EditText InputEmail, InputPassword;
    private Button signInButton;
    private ProgressDialog loadingBar;
    private TextView adminLink, NotAdminLink;

    private String parentDbName = "Users";
    private CheckBox checkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);

        signInButton = (Button)findViewById(R.id.signIn_btn);
        InputEmail = (EditText) findViewById(R.id.login_email_input);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink = (TextView) findViewById(R.id.Not_admin_panel_link);

        loadingBar = new ProgressDialog(this);

        checkbox = (CheckBox) findViewById(R.id.checkBox);
        Paper.init(this);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser();
            }
        });
        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInButton.setText("Signin admin");
                adminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "admins";
            }
        });
        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInButton.setText("Signin");
                adminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }
    private void signInUser(){
        String email = InputEmail.getText().toString();
        final String email2= email.replace(".","1");

        String password = InputPassword.getText().toString();

         if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your write email...",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your write password...",Toast.LENGTH_LONG).show();
        }
        else {
             loadingBar.setTitle("SignInButton");
             loadingBar.setMessage("please wait, while we are checking the credentials.");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();
             AllowAccessToAccount(email2, password);
         }
    }
    private void AllowAccessToAccount(final String email, final String password){
        if (checkbox.isChecked()){
            Paper.book().write(prevalent.UserEmailKey, email);
            Paper.book().write(prevalent.UserPasswordKey, password);
        }
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.child("Users").child(email).exists()){
                    Users usersData = datasnapshot.child("Users").child(email).getValue(Users.class);
                    if (usersData.getEmail().equals(email)){
                        if (usersData.getPassword().equals(password)){
                          if(parentDbName.equals("admins")) {
                              Toast.makeText(Screen1.this, "Welcome Admin, you are logged in Successfully", Toast.LENGTH_SHORT).show(); loadingBar.dismiss();
                              loadingBar.dismiss();
                              Intent intent = new Intent(Screen1.this, adminProductTypeActivity.class);
                              startActivity(intent);
                          }
                          else if(parentDbName.equals("Users")){
                              Toast.makeText(Screen1.this, "logged in Successfully", Toast.LENGTH_SHORT).show(); loadingBar.dismiss();
                              loadingBar.dismiss();
                              Intent intent = new Intent(Screen1.this, Main2Activity.class);
                              prevalent.currentOnlineUser = usersData;
                              startActivity(intent);
                          }
                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(Screen1.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(Screen1.this, "Account with this" + email + "do not exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}




