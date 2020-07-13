package com.example.book;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

   Button Update_profile;
   EditText User_name,User_email;
   ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        loadingBar = new ProgressDialog(this);
        User_name=findViewById(R.id.User_name);
        User_email=findViewById(R.id.User_email);
        Update_profile=findViewById(R.id.Update_profile);
        Update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=User_name.getText().toString();
                String email=User_email.getText().toString();
                if (TextUtils.isEmpty(name))
                {
                    Toast.makeText(SettingActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(SettingActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                }
                else {

                    loadingBar.setTitle("Updating");
                    loadingBar.setMessage("please wait.....");
                    loadingBar.setCanceledOnTouchOutside(true);
                    loadingBar.show();
                }

            }
        });

    }
}
