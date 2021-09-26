package com.example.escape.triporganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;

public class ConfirmBooking extends AppCompatActivity {

    TextView Title,BookedSeat,TotalPrice,TrsId,UserPhone;
    Button Cancel,Confirm;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        Intent ConfirmPost=getIntent();
        final String bookedKey =ConfirmPost.getStringExtra("BookKey");

        toolbar = findViewById(R.id.toolbar_booked);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Title=findViewById(R.id.title_post);
        BookedSeat=findViewById(R.id.booked_seat);
        TotalPrice=findViewById(R.id.total_price);
        TrsId=findViewById(R.id.trs_id);
        UserPhone=findViewById(R.id.user_phone);
        Cancel=findViewById(R.id.cancel_book);
        Confirm=findViewById(R.id.confirm_book);

        ShowBookingInfo(bookedKey);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BookingConfirm(bookedKey);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CancelConfirm(bookedKey);
            }
        });



    }

    private void ShowBookingInfo(String bookedKey) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookPost = database.getReference("BookedPost").child("TripOrgView");

        bookPost.child(bookedKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    Title.setText(snapshot.child("PostTitle").getValue().toString());
                    BookedSeat.setText(snapshot.child("BookingSeat").getValue().toString());
                    TotalPrice.setText(snapshot.child("TotalPrice").getValue().toString());
                    TrsId.setText(snapshot.child("TrsId").getValue().toString());
                    UserPhone.setText(snapshot.child("UserPhone").getValue().toString());

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void BookingConfirm(final String bookedKey) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookPost = database.getReference("BookedPost").child("UserView");

//        HashMap<String, Object> Update = new HashMap<>();
//        Update.put("State", "Confirmed");
        bookPost.child(bookedKey).child("State").setValue("Confirmed").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                DatabaseReference DeletePost = database.getReference("BookedPost").child("TripOrgView");
                DeletePost.child(bookedKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        AvailableSeatCount(bookedKey);
//                        Intent intent=new Intent(ConfirmBooking.this,ShowBookingPost.class);
//                        startActivity(intent);
//                        Toast.makeText(ConfirmBooking.this,"Confirmed Successfully" , Toast.LENGTH_SHORT).show();
//                        finish();
                    }
                });

            }
        });

    }

    private void AvailableSeatCount(String bookedKey) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookseat = database.getReference("BookedPost").child("UserView");

        bookseat.child(bookedKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String Postkey = snapshot.child("PostKey").getValue().toString();

                String Aseat = snapshot.child("AvailableSeat").getValue().toString();
                int aSeat = Integer.valueOf(Aseat);

                String Bseat = snapshot.child("BookingSeat").getValue().toString();
                int bSeat = Integer.valueOf(Bseat);

                  int availSeat = (aSeat - bSeat);
                 final String AvailableSeat= String.valueOf(availSeat);

                DatabaseReference userViewRef = FirebaseDatabase.getInstance().getReference().child("UserPostView").child(Postkey);
                userViewRef.child("AvailableSeat").setValue(AvailableSeat).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        String Uid =FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DatabaseReference blogPost = FirebaseDatabase.getInstance().getReference().child("Blog Post").child(Uid).child(Postkey);
                        blogPost.child("AvailableSeat").setValue(AvailableSeat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Intent intent=new Intent(ConfirmBooking.this,ShowBookingPost.class);
                                startActivity(intent);
                                Toast.makeText(ConfirmBooking.this,"Confirmed Successfully" , Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ConfirmBooking.this,"Failed" , Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void CancelConfirm(final String bookedKey) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference bookPost = database.getReference("BookedPost").child("UserView");

        bookPost.child(bookedKey).child("State").setValue("Canceled").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                DatabaseReference DeletePost = database.getReference("BookedPost").child("TripOrgView");
                DeletePost.child(bookedKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent intent=new Intent(ConfirmBooking.this,ShowBookingPost.class);
                        startActivity(intent);
                        Toast.makeText(ConfirmBooking.this,"Cancelled" , Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(ConfirmBooking.this, ShowBookingPost.class);
        startActivity(intent);
        finish();
        return true;
    }
}