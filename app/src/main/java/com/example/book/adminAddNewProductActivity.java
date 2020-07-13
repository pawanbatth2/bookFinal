package com.example.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class adminAddNewProductActivity extends AppCompatActivity {
private String Product, Detail, Price, BTitle, saveCurrentDate, saveCurrentTime;
private Button AddProductButton;
private ImageView BookIconImage;
private EditText BookTitle, BookDetail, BookPrice;
private  static final int GalleryPick = 1;
private Uri ImageUrl;
private String ProductRandomKey, downloadImageUrl;
private StorageReference ProductImageRef;
private DatabaseReference ProductsRef;
private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);
        loadingBar = new ProgressDialog(this);
        Product = getIntent().getExtras().get("product").toString();
        ProductImageRef = FirebaseStorage.getInstance().getReference().child("Book Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Book Images");
        AddProductButton = (Button) findViewById(R.id.Add_New_Product);
        BookIconImage = (ImageView) findViewById(R.id.book_icon);
        BookTitle = (EditText) findViewById(R.id.book_title);
        BookDetail = (EditText) findViewById(R.id.book_detail);
        BookPrice = (EditText) findViewById(R.id.book_price);

        BookIconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        AddProductButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                validateProductData();
            }
        });
    }
    private void OpenGallery(){
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null){
            ImageUrl = data.getData();
            BookIconImage.setImageURI(ImageUrl);
        }
    }
    private void validateProductData(){
       Detail = BookDetail.getText().toString();
       Price = BookPrice.getText().toString();
       BTitle = BookTitle.getText().toString();

       if(ImageUrl == null){
           Toast.makeText(this, "Book image is mandatory", Toast.LENGTH_SHORT).show();
       }
       else if(TextUtils.isEmpty(Detail)){
            Toast.makeText(this, "Please write book detail", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)){
            Toast.makeText(this, "Please write book price", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(BTitle)){
            Toast.makeText(this, "Please write book Title", Toast.LENGTH_SHORT).show();
        }
       else{
           storeBookInformation();
       }
    }

    private void storeBookInformation() {
        loadingBar.setTitle("Add Product");
        loadingBar.setMessage("Dear Admin, please wait, while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM_dd_yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        ProductRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImageRef.child(ImageUrl.getLastPathSegment() + ProductRandomKey);

        final UploadTask uploadTask = filePath.putFile(ImageUrl);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(adminAddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(adminAddNewProductActivity.this, "Image Uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }

                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(adminAddNewProductActivity.this, "got the Book image successfully...", Toast.LENGTH_SHORT).show();
                       SaveProductInfoToDatabase();
                        }
                    }
                });
            }

        });
    }
    private void SaveProductInfoToDatabase(){
        DatabaseReference databaseReference;
        databaseReference= FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object>productMap = new HashMap<>();
        productMap.put("pid", ProductRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("detail", BookDetail.getText().toString());
        productMap.put("image", downloadImageUrl);
        productMap.put("product", Product);
        productMap.put("price", BookPrice.getText().toString());
        productMap.put("BTitle", BookTitle.getText().toString());

//        databaseReference.child("profiles").child(currentUser).setValue(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful())
//                {
////                    Intent intent=new Intent(SettingsActivity.this,MainActivity.class);
////                    startActivity(intent);
//                    showSnackBar("account updated");
//                    profileManager = ProfileManager.getInstance(context.getApplicationContext());
//                    profileManager.addRegistrationToken(deviceID,currentUser);
//                    // Toast.makeText(EditProfileActivity.this, "account updated", Toast.LENGTH_SHORT).show();
//                    sendDataToServer();
//
//                }
//                else
//                {
//                    String message=task.getException().toString();
//                    showSnackBar("error"+message);
//                    //   Toast.makeText(EditProfileActivity.this, "error"+message, Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
//            }
//        });


        databaseReference.child("Books").child(ProductRandomKey).setValue(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(adminAddNewProductActivity.this, adminProductTypeActivity.class);
                    startActivity(intent);
                    loadingBar.dismiss();
                    Toast.makeText(adminAddNewProductActivity.this, "Product is added successfully...", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(adminAddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
