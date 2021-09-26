package com.example.escape.triporganizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.escape.GuestScreen;
import com.example.escape.R;
import com.example.escape.model.TripOrganizer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class TripOrganigerRegister extends AppCompatActivity {

    TextView login;
    EditText Email,Password,FullName,businnesslicenceNo,PhoneNo;
    Button register,Camera1,Camera2;
    ImageView FrontNid,BackNid,Back;
    ProgressBar progressBar;
    Toolbar toolbar;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Uri imageUri = null;
    public Uri ImageUri1=null;
    private Uri ImageUri2=null;

    private int imageNo;
    private StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_organiger_register);

        mAuth=FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar_reg);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Back=findViewById(R.id.img_back);
        login = findViewById(R.id.loginText);
        progressBar=findViewById(R.id.registerProgress);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        FullName = findViewById(R.id.fullname);
        businnesslicenceNo = findViewById(R.id.licenceNo);
        PhoneNo = findViewById(R.id.phoneno);
        register = findViewById(R.id.register);
        Camera1=findViewById(R.id.button_front);
        Camera2=findViewById(R.id.button_back);
        FrontNid=findViewById(R.id.nid_front);
        BackNid=findViewById(R.id.nid_back);

        progressDialog = new ProgressDialog(this);

        AlertDialog.Builder builder= new AlertDialog.Builder(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TripOrganigerRegister.this, TripOrganizerLogin.class);
                startActivity(intent);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterTripOrganizer();
            }
        });

        Camera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageNo=1;
                CropImage.activity().start(TripOrganigerRegister.this);

//                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });
        Camera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageNo=2;
                CropImage.activity().start(TripOrganigerRegister.this);
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripOrganigerRegister.this, GuestScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void RegisterTripOrganizer() {

        final String email=Email.getText().toString().trim();
        final String fullname=FullName.getText().toString().trim();
        final String phoneno=PhoneNo.getText().toString().trim();
        final String licenceno=businnesslicenceNo.getText().toString().trim();
        final String password=Password.getText().toString().trim();

        if (email.isEmpty()){
            Email.setError("Field can't be empty");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Please provide valid Email");
            Email.requestFocus();
            return;

        }
        if (fullname.isEmpty()){
            FullName.setError("Field can't be empty");
            FullName.requestFocus();
            return;
        }
        if (phoneno.isEmpty()){
            PhoneNo.setError("Field can't be empty");
            PhoneNo.requestFocus();
            return;
        }
        if (phoneno.length()<11){
            PhoneNo.setError("Mobile number not valid");
            PhoneNo.requestFocus();
            return;
        }
        if (licenceno.isEmpty()){
            businnesslicenceNo.setError("Field can't be empty");
            businnesslicenceNo.requestFocus();
            return;
        }
        if (password.isEmpty()){
            Password.setError("Field can't be empty");
            Password.requestFocus();
            return;
        }
        if (password.length()<6){
            Password.setError("Password length should be 6 character!");
            Password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        register.setText("");
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                            user.sendEmailVerification();

                            ShowAlertDialog(email,fullname,phoneno,licenceno,password);
                            progressBar.setVisibility(View.GONE);

                        }else{

                            Toast.makeText(TripOrganigerRegister.this, "Failed to register.Try again!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });

    }

    private void ShowAlertDialog(final String email, final String fullname, final String phoneno, final String licenceno, final String password) {

        final FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        AlertDialog.Builder Builder = new AlertDialog.Builder(TripOrganigerRegister.this);
        Builder.setTitle("Email Verification");
        Builder
                .setMessage("A Verification link is sent to your Email.Please Verify and click Refresh")
                .setCancelable(false)
                .setPositiveButton("Refresh",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id){

                        FirebaseAuth.getInstance().getCurrentUser().reload()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            if (user.isEmailVerified()){

                                                UploadVerificationImg(email,fullname,phoneno,licenceno,password);


                                            }else{

                                                ShowAlertDialog(email,fullname,phoneno,licenceno,password);
                                                Toast.makeText(TripOrganigerRegister.this, "Check your Email for verification", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }
                                });

                    }
                }).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                // if this button is clicked, just close
                // the dialog box and do nothing
                dialog.cancel();
                progressBar.setVisibility(View.GONE);
            }
        });
        AlertDialog dialog = Builder.create();
        dialog.show();



    }

    private void UploadVerificationImg(final String email, final String fullname, final String phoneno, final String licenceno, final String password) {

        progressBar.setVisibility(View.VISIBLE);
        StorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference imgPath = StorageRef.child("VerificationImg").child(ImageUri1.getLastPathSegment());
        imgPath.putFile(ImageUri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final Uri FrontNid = uri;
                        final String frontnid =FrontNid.toString();

                        final StorageReference imgPath = StorageRef.child("VerificationImg").child(ImageUri2.getLastPathSegment());
                        imgPath.putFile(ImageUri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imgPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        final Uri BackNid = uri;
                                        String backnid =BackNid.toString();

                                        UploadOrganizerInfo(email,fullname,phoneno,licenceno,password,frontnid,backnid);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        register.setText("Register");

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(TripOrganigerRegister.this, "Failed to upload.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(TripOrganigerRegister.this, "Failed to upload.", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void UploadOrganizerInfo(String email, String fullname, String phoneno, String licenceno, String password, String frontnid, String backnid) {

        progressBar.setVisibility(View.VISIBLE);
        TripOrganizer tripOrganizer =new TripOrganizer(email,fullname,phoneno,licenceno,password,frontnid,backnid);

        FirebaseDatabase.getInstance().getReference("TripOrganizer")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(tripOrganizer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()) {

                    String userUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference blockid= FirebaseDatabase.getInstance().getReference().child("Blocked").child("Organizer");

                    blockid.child(userUid).child("State").setValue("False").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressBar.setVisibility(View.INVISIBLE);

                            VerifyOrg();

                        }
                    });

                } else {

                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(TripOrganigerRegister.this, "Failed to register.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void VerifyOrg() {

        progressBar.setVisibility(View.VISIBLE);
        String userUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference verifyRef= FirebaseDatabase.getInstance().getReference().child("TripOrganizer").child(userUid);

        verifyRef.child("Status").setValue("Not Verified").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String userUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference verifyRef= FirebaseDatabase.getInstance().getReference().child("OrgVerification").child(userUid);

                verifyRef.child("Status").setValue("Not Verified").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(TripOrganigerRegister.this, "Register Successful.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TripOrganigerRegister.this, TripOrganizerLogin.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data !=null){

            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            Uri imageUri =result.getUri();

            switch (imageNo){
                case (1):
                    //here you have resultUri for save image or preview as image1
                    ImageUri1=imageUri;
                    FrontNid.setImageURI(ImageUri1);
                    break;
                case (2):
                    //here you have resultUri for save image or preview as image1
                    ImageUri2=imageUri;
                    BackNid.setImageURI(ImageUri2);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


//    private void alertDialog() {
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("hjdsgfhdsdgf");
//        builder.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                FirebaseAuth.getInstance().getCurrentUser().reload()
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
//                        if (user.isEmailVerified()) {
//
////                          Toast.makeText(TripOrganigerRegister.this, "Register Sucessfully", Toast.LENGTH_SHORT).show();
//
//                            Intent intent = new Intent(TripOrganigerRegister.this, TripOrganizerLogin.class);
//                            startActivity(intent);
//                            finish();
//
//                        } else {
//
//                            Toast.makeText(TripOrganigerRegister.this, "Failed to register.Try again!", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//                });
//
//            }
//        });
//    }
}