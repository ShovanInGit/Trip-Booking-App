package com.example.escape.triporganizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.escape.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UpdatePostDetails extends AppCompatActivity {

    ImageButton upImage;
    EditText upTitle, upDesc,uptotalSeat,upavailableSeat,upCaution,upstartDate,upreturnDate,upprice;
    Button update;
    ProgressDialog progressDialog;
    Toolbar toolbar;

    private static final int GALLERY_REQUEST = 5;

    private Uri imageUri = null;

    private StorageReference StorageRef;
    private DatabaseReference DatabaseRef;

    boolean clicked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post_details);

        StorageRef = FirebaseStorage.getInstance().getReference();
        DatabaseRef = FirebaseDatabase.getInstance().getReference();

        upImage = findViewById(R.id.UpdateImage);
        upTitle = findViewById(R.id.UpdateTitle);
        upDesc = findViewById(R.id.UpdateDesc);
        update = findViewById(R.id.Update);

        toolbar = findViewById(R.id.toolbar_upd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        upstartDate=findViewById(R.id.upStartingDate);
        upreturnDate=findViewById(R.id.upReturningDate);
        uptotalSeat=findViewById(R.id.upPostSeat);
        upavailableSeat=findViewById(R.id.upPostAvailableSeat);
        upCaution=findViewById(R.id.upCaution);
        upprice=findViewById(R.id.upprice);

        progressDialog = new ProgressDialog(this);

        Intent updatePost = getIntent();
        final String ItemKey = updatePost.getStringExtra("itemKey");
        final String CurrentImage=updatePost.getStringExtra("currentImg");



        upImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicked=true;

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);


            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdatePost(ItemKey,CurrentImage);
            }
        });

        PostInfoDisplay(ItemKey,upTitle,upDesc,upImage);
    }

    private void PostInfoDisplay(String itemKey, final EditText upTitle, final EditText upDesc, final ImageButton upImage) {

        String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference displayPost = DatabaseRef.child("Blog Post").child(CurrentUid).child(itemKey);

        displayPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    String title =snapshot.child("Title").getValue().toString();
                    String image =snapshot.child("PostImage").getValue().toString();
                    String desc =snapshot.child("Description").getValue().toString();

                    String tSeat =snapshot.child("TotalSeat").getValue().toString();
                    String starttd =snapshot.child("StartTD").getValue().toString();
                    String rettd =snapshot.child("ReturnTD").getValue().toString();
                    String ppperson =snapshot.child("PricePerPerson").getValue().toString();
                    String availseat =snapshot.child("AvailableSeat").getValue().toString();
                    String caution =snapshot.child("Caution").getValue().toString();

                    upTitle.setText(title);
                    upDesc.setText(desc);
                    upstartDate.setText(starttd);
                    upreturnDate.setText(rettd);
                    uptotalSeat.setText(tSeat);
                    upavailableSeat.setText(availseat);
                    upCaution.setText(caution);
                    upprice.setText(ppperson);
                    Picasso.get().load(image).into(upImage);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void UpdatePost(final String itemKey, final String currentImage) {


        if(clicked)
        {
            GetDownloadUrl(itemKey);

        } else
        {
            GetCurrentUrl(itemKey,currentImage);
        }

    }

    private void GetCurrentUrl(final String itemKey, final String currentImage) {

        progressDialog.setMessage("Updating Post...");
        progressDialog.show();

        final String title = upTitle.getText().toString().trim();
        final String desc = upDesc.getText().toString().trim();
        final String tSeat = uptotalSeat.getText().toString().trim();
        final String starttd = upstartDate.getText().toString().trim();
        final String rettd = upreturnDate.getText().toString().trim();
        final String ppperson = upprice.getText().toString().trim();
        final String availseat = upavailableSeat.getText().toString().trim();
        final String caution = upCaution.getText().toString().trim();

        if (title.isEmpty()) {
            upTitle.setError("Field can't be empty");
            upTitle.requestFocus();
            return;
        }
        if (desc.isEmpty()) {
            upDesc.setError("Field can't be empty");
            upDesc.requestFocus();
            return;
        }
        if (tSeat.isEmpty()) {
            uptotalSeat.setError("Field can't be empty");
            uptotalSeat.requestFocus();
            return;
        }
        if (starttd.isEmpty()) {
            upstartDate.setError("Field can't be empty");
            upstartDate.requestFocus();
            return;
        }
        if (rettd.isEmpty()) {
            upreturnDate.setError("Field can't be empty");
            upreturnDate.requestFocus();
            return;
        }
        if (ppperson.isEmpty()) {
            upprice.setError("Field can't be empty");
            upprice.requestFocus();
            return;
        }
        if (availseat.isEmpty()) {
            upavailableSeat.setError("Field can't be empty");
            upavailableSeat.requestFocus();
            return;
        }
        if (caution.isEmpty()) {
            upCaution.setError("Field can't be empty");
            upCaution.requestFocus();
            return;
        }

        final String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference updatePost = DatabaseRef.child("Blog Post").child(CurrentUid);

        HashMap<String, Object> postInfo = new HashMap<>();
        postInfo.put("Title", title);
        postInfo.put("Description", desc);
        postInfo.put("TotalSeat", tSeat);
        postInfo.put("StartTD", starttd);
        postInfo.put("ReturnTD", rettd);
        postInfo.put("PricePerPerson", ppperson);
        postInfo.put("AvailableSeat", availseat);
        postInfo.put("Caution", caution);
        updatePost.child(itemKey).updateChildren(postInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

//                progressDialog.dismiss();

                DatabaseReference upPost = DatabaseRef.child("UserPostView");
                HashMap<String, Object> postInfo = new HashMap<>();
                postInfo.put("Title", title);
                postInfo.put("Description", desc);
                postInfo.put("TotalSeat", tSeat);
                postInfo.put("StartTD", starttd);
                postInfo.put("ReturnTD", rettd);
                postInfo.put("PricePerPerson", ppperson);
                postInfo.put("AvailableSeat", availseat);
                postInfo.put("Caution", caution);
                upPost.child(itemKey).updateChildren(postInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();

                        Intent intent=new Intent(UpdatePostDetails.this, TripOrganizerPostView.class);
                        startActivity(intent);
                        finish();
                    }
                });

//                Intent intent=new Intent(UpdatePostDetails.this, TripOrganizerPostView.class);
//                startActivity(intent);
//                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void GetDownloadUrl(final String itemKey) {

        progressDialog.setMessage("Updating Post...");
        progressDialog.show();

        final String title = upTitle.getText().toString().trim();
        final String desc = upDesc.getText().toString().trim();
        final String tSeat = uptotalSeat.getText().toString().trim();
        final String starttd = upstartDate.getText().toString().trim();
        final String rettd = upreturnDate.getText().toString().trim();
        final String ppperson = upprice.getText().toString().trim();
        final String availseat = upavailableSeat.getText().toString().trim();
        final String caution = upCaution.getText().toString().trim();



        if (title.isEmpty()) {
            upTitle.setError("Field can't be empty");
            upTitle.requestFocus();
            return;
        }
        if (desc.isEmpty()) {
            upDesc.setError("Field can't be empty");
            upDesc.requestFocus();
            return;
        }
        if (imageUri == null) {

            Toast.makeText(UpdatePostDetails.this, "Please,Select an Image", Toast.LENGTH_SHORT).show();
        }
        if (tSeat.isEmpty()) {
            uptotalSeat.setError("Field can't be empty");
            uptotalSeat.requestFocus();
            return;
        }
        if (starttd.isEmpty()) {
            upstartDate.setError("Field can't be empty");
            upstartDate.requestFocus();
            return;
        }
        if (rettd.isEmpty()) {
            upreturnDate.setError("Field can't be empty");
            upreturnDate.requestFocus();
            return;
        }
        if (ppperson.isEmpty()) {
            upprice.setError("Field can't be empty");
            upprice.requestFocus();
            return;
        }
        if (availseat.isEmpty()) {
            upavailableSeat.setError("Field can't be empty");
            upavailableSeat.requestFocus();
            return;
        }
        if (caution.isEmpty()) {
            upCaution.setError("Field can't be empty");
            upCaution.requestFocus();
            return;
        }

        final StorageReference imagePath = StorageRef.child("Post Images").child(imageUri.getLastPathSegment());
        final String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference updatePost = DatabaseRef.child("Blog Post").child(CurrentUid);

        imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final Uri downloadImg = uri;
                        progressDialog.dismiss();

                        updatePost.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()){

//                                    DatabaseReference UpdatePost = updatePost.child(itemKey);
//                                    UpdatePost.child("Title").setValue(title);
//                                    UpdatePost.child("Description").setValue(desc);
//                                    UpdatePost.child("PostImage").setValue(downloadImg.toString());
//
//                                    DeleteBeforeImage();
//
//                                    Intent intent=new Intent(UpdatePostDetails.this, TripOrganizerPostView.class);
//                                    startActivity(intent);
//                                    finish();
                                    HashMap<String, Object> postInfo = new HashMap<>();
                                    postInfo.put("Title", title);
                                    postInfo.put("Description", desc);
                                    postInfo.put("TotalSeat", tSeat);
                                    postInfo.put("StartTD", starttd);
                                    postInfo.put("ReturnTD", rettd);
                                    postInfo.put("PricePerPerson", ppperson);
                                    postInfo.put("AvailableSeat", availseat);
                                    postInfo.put("Caution", caution);
                                    postInfo.put("PostImage", downloadImg.toString());

                                    updatePost.child(itemKey).updateChildren(postInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            DatabaseReference upPost = DatabaseRef.child("UserPostView");
                                            HashMap<String, Object> postInfo = new HashMap<>();
                                            postInfo.put("Title", title);
                                            postInfo.put("Description", desc);
                                            postInfo.put("TotalSeat", tSeat);
                                            postInfo.put("StartTD", starttd);
                                            postInfo.put("ReturnTD", rettd);
                                            postInfo.put("PricePerPerson", ppperson);
                                            postInfo.put("AvailableSeat", availseat);
                                            postInfo.put("Caution", caution);
                                            postInfo.put("PostImage", downloadImg.toString());

                                            upPost.child(itemKey).updateChildren(postInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    DeleteBeforeImage();

                                                    Intent intent = new Intent(UpdatePostDetails.this, TripOrganizerPostView.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        finish();
                        return;
                    }
                });
            }
        });


    }

    private void DeleteBeforeImage() {

        Intent updatePost = getIntent();
        final String CurrentImg = updatePost.getStringExtra("currentImg");

        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(CurrentImg);
        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){

            imageUri =data.getData();
            upImage.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(UpdatePostDetails.this, TripOrganizerPostView.class);
        startActivity(intent);
        finish();
        return true;
    }
}

