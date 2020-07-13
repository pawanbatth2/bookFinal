
package com.example.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import javax.xml.validation.Validator;

public class Screen3 extends AppCompatActivity {
    private Button signUpButton;
    private EditText InputFirstName, InputLastName, InputEmail, InputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen3);

        signUpButton = (Button)findViewById(R.id.signUp_btn);
        InputFirstName = (EditText) findViewById(R.id.firstName_input);
        InputLastName = (EditText) findViewById(R.id.LastName_input);
        InputEmail = (EditText) findViewById(R.id.register_email_input);


        InputPassword = (EditText) findViewById(R.id.register_password_input);
        loadingBar = new ProgressDialog(this);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp() {
        String firstName = InputFirstName.getText().toString();
        String LastName = InputLastName.getText().toString();
        String email = InputEmail.getText().toString();

        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(firstName)){
            Toast.makeText(this, "Please enter your write first name...",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(LastName)){
            Toast.makeText(this, "Please enter your write last name...",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your write email...",Toast.LENGTH_LONG).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your write password...",Toast.LENGTH_LONG).show();
        }
        else{
            loadingBar.setTitle("signUp");
            loadingBar.setMessage("please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            Validateemail(firstName, LastName, email, password);
        }
}

private void Validateemail(final String firstName, final String LastName, final String email, final String password){
   final String email2= email.replace(".","1");

    final DatabaseReference RootRef;
    RootRef = FirebaseDatabase.getInstance().getReference();

    RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (!(dataSnapshot.child("Users").child(email2).exists())) {
                HashMap<String, Object> userdataMap = new HashMap<>();
                userdataMap.put("email", email2);
                userdataMap.put("firstName", firstName);
                userdataMap.put("LastName", LastName);
                userdataMap.put("password", password);

                RootRef.child("Users").child(email2).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>(){
                  public void onComplete(@NonNull Task<Void>task){
                      if (task.isSuccessful()){
                          Toast.makeText(Screen3.this, "Congratulation, your account has been created", Toast.LENGTH_SHORT).show();
                      loadingBar.dismiss();

                          Intent intent = new Intent(Screen3.this, Screen1.class);
                          startActivity(intent);
                      }
                      else{
                          loadingBar.dismiss();
                          Toast.makeText(Screen3.this, "Network error: please try again", Toast.LENGTH_SHORT).show();
                      }
                  }
                });
            }
            else {
                Toast.makeText(Screen3.this, "This" + email + "already exists.", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                Toast.makeText(Screen3.this, "Please try again using another email.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Screen3.this, MainActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
    }
}


