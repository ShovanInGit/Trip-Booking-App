package com.example.escape.triporganizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.escape.R;
import com.example.escape.model.TripOrganizer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfile extends AppCompatActivity {

    CircleImageView profileImg;
    CircleImageView Image;
    ImageView addImg,profileEdit;
    EditText upFullName,upAdress,upPhoneNo;
    Button Update;
    ProgressBar progressBar;
    Toolbar toolbar;
    TextView name,address,Phone;
    ConstraintLayout showProfile,hideProfile;


    boolean clicked=false;
    private static final int GALLERY_REQUEST = 7;
    private Uri imageUri = null;

    private StorageReference StorageRef;
    private DatabaseReference DatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        StorageRef = FirebaseStorage.getInstance().getReference();
        DatabaseRef = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileImg=findViewById(R.id.profile_image);
        addImg=findViewById(R.id.add_image);
        progressBar=findViewById(R.id.up_progress);
        upFullName = findViewById(R.id.up_fullname);
        upAdress = findViewById(R.id.up_address);
        upPhoneNo = findViewById(R.id.up_phoneno);
        Update = findViewById(R.id.update);

        name = findViewById(R.id.show_name);
        address = findViewById(R.id.show_address);
        Image = findViewById(R.id.show_img);
        Phone = findViewById(R.id.show_phone);
        profileEdit=findViewById(R.id.profile_edit);
        showProfile=findViewById(R.id.show_profile);
        hideProfile=findViewById(R.id.hide_profile);

        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked=true;

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(UpdateProfile.this);
//                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clicked)
                {
                    UpdateUserWithImg();

                } else
                {
                    updateUserInfoOnly();
                }


            }
        });
        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProfile.setVisibility(View.INVISIBLE);
                hideProfile.setVisibility(View.VISIBLE);

            }
        });

        EdittextinfoDisplay(upFullName,upAdress,upPhoneNo,profileImg);
        infoDisplay(name,address,Image,Phone);

    }

    private void infoDisplay(final TextView name, final TextView address, final CircleImageView image, final TextView phone) {
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference infoShow=database.getReference("TripOrganizer").child(CurrentUid);
        infoShow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    if (snapshot.child("image").exists()) {

                        String fullname = snapshot.child("fullname").getValue().toString();
                        String Address = snapshot.child("address").getValue().toString();
                        String Phone = snapshot.child("phoneno").getValue().toString();
                        String Image = snapshot.child("image").getValue().toString();

                        name.setText(fullname);
                        address.setText(Address);
                        phone.setText(Phone);
                        Picasso.get().load(Image).into(image);

                    }else {

                        if (snapshot.child("address").exists()){

                            String fullname = snapshot.child("fullname").getValue().toString();
                            String Address = snapshot.child("address").getValue().toString();
                            String Phone = snapshot.child("phoneno").getValue().toString();

                            name.setText(fullname);
                            address.setText(Address);
                            phone.setText(Phone);

                        }else {

                            String fullname = snapshot.child("fullname").getValue().toString();
                            String Phone = snapshot.child("phoneno").getValue().toString();

                            name.setText(fullname);
                            phone.setText(Phone);

                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void EdittextinfoDisplay(final EditText upFullName, final EditText upAdress, final EditText upPhoneNo, final CircleImageView profileImg) {

        FirebaseDatabase database= FirebaseDatabase.getInstance();

        String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference infoShow=database.getReference("TripOrganizer").child(CurrentUid);

        infoShow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    if (snapshot.child("image").exists()) {

                            String fullname = snapshot.child("fullname").getValue().toString();
                            String address = snapshot.child("address").getValue().toString();
                            String phone = snapshot.child("phoneno").getValue().toString();
                            String image = snapshot.child("image").getValue().toString();

                            upFullName.setText(fullname);
                            upAdress.setText(address);
                            upPhoneNo.setText(phone);
                            Picasso.get().load(image).into(profileImg);

                    }else {

                        if (snapshot.child("address").exists()){

                            String fullname = snapshot.child("fullname").getValue().toString();
                            String phone = snapshot.child("phoneno").getValue().toString();
                            String address = snapshot.child("address").getValue().toString();

                            upFullName.setText(fullname);
                            upPhoneNo.setText(phone);
                            upAdress.setText(address);

                        }else {

                            String fullname = snapshot.child("fullname").getValue().toString();
                            String phone = snapshot.child("phoneno").getValue().toString();

                            upFullName.setText(fullname);
                            upPhoneNo.setText(phone);

                        }


                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void UpdateUserWithImg(){


        final FirebaseDatabase database= FirebaseDatabase.getInstance();

        //getting the values to save
        final String Fullname = upFullName.getText().toString();
        final String address = upAdress.getText().toString();
        final String phone = upPhoneNo.getText().toString();

        final StorageReference imagePath = StorageRef.child("Profile Images").child(imageUri.getLastPathSegment());
        String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference upRef=database.getReference("TripOrganizer").child(CurrentUid);


            upRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {

                        if (snapshot.child("image").exists()) {

                            String img = snapshot.child("image").getValue().toString();
                            final String[] oldImg = {img};

                            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(oldImg[0]);
                            storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    final Uri downloadImg = uri;
                                                    oldImg[0] =null;

                                                    if (!TextUtils.isEmpty(Fullname) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(phone)) {
                                                        //it will create a unique id and we will use it as the Primary Key for our user

                                                        HashMap<String, Object> organizerInfo = new HashMap<>();
                                                        organizerInfo.put("fullname", Fullname);
                                                        organizerInfo.put("phoneno", phone);
                                                        organizerInfo.put("address", address);
                                                        organizerInfo.put("image", (downloadImg.toString()));


                                                        upRef.updateChildren(organizerInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Intent intent = new Intent(UpdateProfile.this, TripOrganizerPostView.class);
                                                                startActivity(intent);
                                                                Toast.makeText(UpdateProfile.this, "Updated Successful.", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                                showProfile.setVisibility(View.VISIBLE);
                                                                hideProfile.setVisibility(View.INVISIBLE);
                                                            }
                                                        });

                                                    } else {

                                                        if (TextUtils.isEmpty(Fullname)) {

                                                            upFullName.setError("Field cannot be empty");
                                                        } else
                                                            upAdress.setError("Field cannot be empty");
                                                    }
                                                }
                                            });
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                        }else{
                            imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            final Uri downloadImg = uri;

                                            String oldImg=downloadImg.toString();

                                            if (!TextUtils.isEmpty(Fullname) && !TextUtils.isEmpty(address) && !TextUtils.isEmpty(phone)) {
                                                //it will create a unique id and we will use it as the Primary Key for our user

                                                HashMap<String, Object> organizerInfo = new HashMap<>();
                                                organizerInfo.put("fullname", Fullname);
                                                organizerInfo.put("phoneno", phone);
                                                organizerInfo.put("address", address);
                                                organizerInfo.put("image", (downloadImg.toString()));


                                                upRef.updateChildren(organizerInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Intent intent = new Intent(UpdateProfile.this, TripOrganizerPostView.class);
                                                        startActivity(intent);
                                                        Toast.makeText(UpdateProfile.this, "Updated Successful.", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        showProfile.setVisibility(View.VISIBLE);
                                                        hideProfile.setVisibility(View.INVISIBLE);
                                                    }
                                                });

                                            } else {

                                                if (TextUtils.isEmpty(Fullname)) {

                                                    upFullName.setError("Field cannot be empty");
                                                } else
                                                    upAdress.setError("Field cannot be empty");
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void updateUserInfoOnly() {

        FirebaseDatabase database= FirebaseDatabase.getInstance();

        //getting the values to save
        String Fullname = upFullName.getText().toString();
        String address = upAdress.getText().toString();
        String phone = upPhoneNo.getText().toString();

        if (!TextUtils.isEmpty(Fullname) && !TextUtils.isEmpty(address)&& !TextUtils.isEmpty(phone))  {
            //it will create a unique id and we will use it as the Primary Key for our user

           String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
           DatabaseReference upRef=database.getReference("TripOrganizer").child(CurrentUid);


            HashMap<String, Object> organizerInfo = new HashMap<>();
            organizerInfo.put("fullname", Fullname);
            organizerInfo.put("phoneno", phone);
            organizerInfo.put("address", address);

            upRef.updateChildren(organizerInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Intent intent = new Intent(UpdateProfile.this, TripOrganizerPostView.class);
                    startActivity(intent);
                    Toast.makeText(UpdateProfile.this, "Updated Successful.", Toast.LENGTH_SHORT).show();
                    finish();

                }
            });

        }else {

            if (TextUtils.isEmpty(Fullname)){

                upFullName.setError("Field cannot be empty");
            }else
                upAdress.setError("Field cannot be empty");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data !=null){

            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            imageUri =result.getUri();
            profileImg.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(UpdateProfile.this, TripOrganizerPostView.class);
        startActivity(intent);
        finish();
        return true;
    }
}