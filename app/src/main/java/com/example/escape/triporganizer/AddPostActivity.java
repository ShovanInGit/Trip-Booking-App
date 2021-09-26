package com.example.escape.triporganizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.escape.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity {

    ImageButton imageButton;
    EditText postTitle,postDescrip,totalSeat,availableSeat,Caution,startDate,returnDate,price,bkash,nogod,rocket;
    Button submitPost;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    TextView VerifyText;
    NestedScrollView Scrollview;

    private Uri imageUri=null;

    private StorageReference StorageRef;
    private DatabaseReference DatabaseRef;

    private static final int GALLERY_REQUEST=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        StorageRef = FirebaseStorage.getInstance().getReference();
        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("Blog Post");

        imageButton=findViewById(R.id.addImage);
        postTitle=findViewById(R.id.TitlePost);
        postDescrip=findViewById(R.id.PostDes);
        VerifyText=findViewById(R.id.verify_text);
        Scrollview=findViewById(R.id.scroll_view);

        toolbar = findViewById(R.id.toolbar_aap);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startDate=findViewById(R.id.StartingDate);
        returnDate=findViewById(R.id.ReturningDate);
        totalSeat=findViewById(R.id.PostSeat);
        availableSeat=findViewById(R.id.PostAvailableSeat);
        Caution=findViewById(R.id.Caution);
        submitPost=findViewById(R.id.Submit);
        price=findViewById(R.id.price);
        bkash=findViewById(R.id.bkash);
        nogod=findViewById(R.id.nogod);
        rocket=findViewById(R.id.rocket);

        CheckVerified();


        progressDialog=new ProgressDialog(this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
        submitPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartPosting();
            }
        });
    }

    private void CheckVerified() {

        String Uid =FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference DataRef = FirebaseDatabase.getInstance().getReference().child("TripOrganizer");

        DataRef.child(Uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    String status = snapshot.child("Status").getValue().toString();
                    if (status.equals("Not Verified")){

                        Scrollview.setVisibility(View.INVISIBLE);
                        VerifyText.setVisibility(View.VISIBLE);
                        submitPost.setVisibility(View.INVISIBLE);

                    }
                    else if (status.equals("Verified")){

                        Scrollview.setVisibility(View.VISIBLE);
                        VerifyText.setVisibility(View.INVISIBLE);
                        submitPost.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void StartPosting() {

        progressDialog.setMessage("Adding Post...");
        progressDialog.show();

        final String title=postTitle.getText().toString().trim();
        final String desc=postDescrip.getText().toString().trim();
        final String tSeat=totalSeat.getText().toString().trim();
        final String availSeat=availableSeat.getText().toString().trim();
        final String startingTD=startDate.getText().toString().trim();
        final String returningTD=returnDate.getText().toString().trim();
        final String caution=Caution.getText().toString().trim();
        final String Price=price.getText().toString().trim();
        final String Bkash=bkash.getText().toString().trim();
        final String Nogod=nogod.getText().toString().trim();
        final String Rocket=rocket.getText().toString().trim();


        if (title.isEmpty()){
            postTitle.setError("Field can't be empty");
            postTitle.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (desc.isEmpty()){
            postDescrip.setError("Field can't be empty");
            postDescrip.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (imageUri == null){

            Toast.makeText(AddPostActivity.this, "Please,Select an Image", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        if (tSeat.isEmpty()){
            totalSeat.setError("Field can't be empty");
            totalSeat.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (availSeat.isEmpty()){
            availableSeat.setError("Field can't be empty");
            availableSeat.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (startingTD.isEmpty()){
            startDate.setError("Field can't be empty");
            startDate.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (returningTD.isEmpty()){
            returnDate.setError("Field can't be empty");
            returnDate.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (Price.isEmpty()){
            price.setError("Field can't be empty");
            price.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if (Bkash.isEmpty()){
            if (Nogod.isEmpty()){
                if (Rocket.isEmpty()){
                    bkash.setError("Give any of these transaction number");
                    bkash.requestFocus();
                    progressDialog.dismiss();
                    return;
                }
            }
        }


        final StorageReference filepath = StorageRef.child("Post Images").child(imageUri.getLastPathSegment());
        final String key =FirebaseAuth.getInstance().getCurrentUser().getUid();

        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final Uri downloadUrl = uri;
                        progressDialog.dismiss();

//                        DatabaseReference AddPost = DatabaseRef.child(key).push();
//                        AddPost.child("Title").setValue(title);
//                        AddPost.child("Description").setValue(desc);
//                        AddPost.child("PostImage").setValue(downloadUrl.toString());

                        Calendar calendar=Calendar.getInstance();
                        SimpleDateFormat currentDate =new SimpleDateFormat("dd MMM,yyyy");
                        final String SaveDate=currentDate.format(calendar.getTime());

                        SimpleDateFormat currentTime =new SimpleDateFormat("HH:mm:ss a");
                        final String SaveTime=currentTime.format(calendar.getTime());

                        DatabaseRef = FirebaseDatabase.getInstance().getReference().child("Blog Post").child(key);

                        final String PostKey=DatabaseRef.push().getKey();


                        final HashMap<String,Object> postAdd= new HashMap<>();
                        postAdd.put("Title",title);
                        postAdd.put("Description",desc);
                        postAdd.put("PostImage",downloadUrl.toString());
                        postAdd.put("PostKey",PostKey);
                        postAdd.put("Date",SaveDate);
                        postAdd.put("Time",SaveTime);

                        postAdd.put("TotalSeat",tSeat);
                        postAdd.put("AvailableSeat",availSeat);
                        postAdd.put("StartTD",startingTD);
                        postAdd.put("Caution",caution);
                        postAdd.put("ReturnTD",returningTD);
                        postAdd.put("PricePerPerson",Price);
                        postAdd.put("Bkash",Bkash);
                        postAdd.put("Nogod",Nogod);
                        postAdd.put("Rocket",Rocket);

                        DatabaseRef.child(PostKey)
                                .updateChildren(postAdd)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                               if (task.isSuccessful()){

                                   DatabaseReference UserView = FirebaseDatabase.getInstance().getReference().child("UserPostView");

                                   final HashMap<String, Object> postAdd = new HashMap<>();
                                   postAdd.put("Title", title);
                                   postAdd.put("Description", desc);
                                   postAdd.put("PostImage", downloadUrl.toString());
                                   postAdd.put("OrganizerKey", key);
                                   postAdd.put("Date", SaveDate);
                                   postAdd.put("Time", SaveTime);

                                   postAdd.put("TotalSeat",tSeat);
                                   postAdd.put("AvailableSeat",availSeat);
                                   postAdd.put("StartTD",startingTD);
                                   postAdd.put("Caution",caution);
                                   postAdd.put("ReturnTD",returningTD);
                                   postAdd.put("PricePerPerson",Price);
                                   postAdd.put("Bkash",Bkash);
                                   postAdd.put("Nogod",Nogod);
                                   postAdd.put("Rocket",Rocket);

                                   UserView.child(PostKey)
                                           .updateChildren(postAdd)
                                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {

                                                   Intent intent = new Intent(AddPostActivity.this, TripOrganizerPostView.class);
                                                   startActivity(intent);
                                                   finish();

                                               }
                                           });

                               }

                           }
                       });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        finish();

                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){

            imageUri =data.getData();
            imageButton.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(AddPostActivity.this, TripOrganizerPostView.class);
        startActivity(intent);
        finish();
        return true;
    }
}