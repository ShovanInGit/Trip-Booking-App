package com.example.escape.triporganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.escape.R;
import com.example.escape.model.OrganizerPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class OrganizerPostDesc extends AppCompatActivity {

    public TextView PostTitle,PostDesc,EventFee,SeatNo,AvailableSeat,StatTimeDate,RetTimeDate,Caution;
    public ImageView postImage;
    Toolbar toolbar;
    DatabaseReference dataRef;

    Animation openRotate,closeRotate,fromBottom,toBottom;
    FloatingActionButton addfab,deletefab,editfab;

    private boolean clicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_post_desc);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PostTitle = findViewById(R.id.post_title);
        PostDesc = findViewById(R.id.post_desc);
        postImage = findViewById(R.id.post_image);
        EventFee = findViewById(R.id.event_fee);
        SeatNo = findViewById(R.id.seat_no);
        AvailableSeat = findViewById(R.id.avilable_seat);
        StatTimeDate = findViewById(R.id.stat_time_date);
        RetTimeDate = findViewById(R.id.ret_time_date);
        Caution = findViewById(R.id.post_caution);

        dataRef= FirebaseDatabase.getInstance().getReference().child("Blog Post");

        Intent intent=getIntent();
        final String CurrImage =intent.getStringExtra("Currimage");
        final String PostKey =intent.getStringExtra("ItemKey");

        getPostDesc(PostKey);

//        String Title=intent.getStringExtra("title");
//        String Description=intent.getStringExtra("desc");
//        byte[] mImage =getIntent().getByteArrayExtra("image");
//        Bitmap bitmap= BitmapFactory.decodeByteArray(mImage,0,mImage.length);


        openRotate = AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim);
        closeRotate = AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim);

        addfab=findViewById(R.id.add_fab);
        deletefab=findViewById(R.id.delete_fab);
        editfab=findViewById(R.id.edit_fab);

        addfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//           Creating method for animation, visibility and clickable of Floating action button
                VisiAnimClick(clicked);
                clicked=!clicked;

            }
        });
        deletefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RemoveItem();
//                Toast.makeText(OrganizerPostDesc.this,"delete clicked" , Toast.LENGTH_SHORT).show();
            }
        });
        editfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent updatePost = new Intent(OrganizerPostDesc.this, UpdatePostDetails.class);
                updatePost.putExtra("itemKey",PostKey);
                updatePost.putExtra("currentImg",CurrImage);
                startActivity(updatePost);
                finish();


//                Toast.makeText(OrganizerPostDesc.this,"edit clicked" , Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void VisiAnimClick(boolean clicked) {

        if (!clicked){
            editfab.setVisibility(View.VISIBLE);
            deletefab.setVisibility(View.VISIBLE);
            editfab.setClickable(true);
            deletefab.setClickable(true);
            editfab.startAnimation(fromBottom);
            deletefab.startAnimation(fromBottom);
            addfab.startAnimation(openRotate);

        }else{
            editfab.setVisibility(View.INVISIBLE);
            deletefab.setVisibility(View.INVISIBLE);
            editfab.setClickable(false);
            deletefab.setClickable(false);
            editfab.startAnimation(toBottom);
            deletefab.startAnimation(toBottom);
            addfab.startAnimation(closeRotate);
        }


    }

//    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////
////        getMenuInflater().inflate(R.menu.add_post_menu, menu);
////        return super.onCreateOptionsMenu(menu);
////    }
////
////    @Override
////    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
////
////
////
////        if (item.getItemId() == R.id.nav_delete) {
////
////        }
////        return super.onOptionsItemSelected(item);
////    }

    private void getPostDesc(String postKey) {

        DatabaseReference userViewRef = FirebaseDatabase.getInstance().getReference().child("UserPostView");

        userViewRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    OrganizerPost userPostDesc = snapshot.getValue(OrganizerPost.class);

                    PostTitle.setText(userPostDesc.getTitle());
                    PostDesc.setText(userPostDesc.getDescription());
                    EventFee.setText(userPostDesc.getPricePerPerson());
                    SeatNo.setText(userPostDesc.getTotalSeat());
                    AvailableSeat.setText(userPostDesc.getAvailableSeat());
                    StatTimeDate.setText(userPostDesc.getStartTD());
                    RetTimeDate.setText(userPostDesc.getReturnTD());
                    Caution.setText(userPostDesc.getCaution());
                    Picasso.get().load(userPostDesc.getPostImage()).into(postImage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void RemoveItem() {

        Intent intent=getIntent();
        final String CurrImage =intent.getStringExtra("Currimage");
        final String itemKey =intent.getStringExtra("ItemKey");

        final String CurrentUid = FirebaseAuth.getInstance().getUid();

        AlertDialog.Builder Builder = new AlertDialog.Builder(OrganizerPostDesc.this);
        Builder.setTitle("Delete Post");
        Builder
                .setMessage("You sure you want to Delete this?")
                .setCancelable(false)
                .setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        dataRef.child(CurrentUid).child(itemKey).removeValue();
                        StorageReference storageRef= FirebaseStorage.getInstance().getReferenceFromUrl(CurrImage);
                        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                DatabaseReference deleteUserView = FirebaseDatabase.getInstance().getReference().child("UserPostView");

                                deleteUserView.child(itemKey)
                                        .removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(OrganizerPostDesc.this,"Post deleted Successfully" , Toast.LENGTH_SHORT).show();

                                    }
                                });

//                                Toast.makeText(OrganizerPostDesc.this,"Post deleted Successfully" , Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                        Intent i = new Intent(OrganizerPostDesc.this, TripOrganizerPostView.class);
                        startActivity(i);
                        finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = Builder.create();
        dialog.show();


    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(OrganizerPostDesc.this, TripOrganizerPostView.class);
        startActivity(intent);
        finish();
        return true;
    }
}