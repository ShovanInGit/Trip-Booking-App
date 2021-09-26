package com.example.escape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.escape.triporganizer.BlockedScreen;
import com.example.escape.triporganizer.TripOrganizerLogin;
import com.example.escape.triporganizer.TripOrganizerPostView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GuestScreen extends AppCompatActivity {

    Button tripOrganizerLogin;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_screen);


        tripOrganizerLogin=findViewById(R.id.tripOrganizer);
        progressBar=findViewById(R.id.login_progress);


        tripOrganizerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrgLogin();

//                Intent intent=new Intent(GuestScreen.this, TripOrganizerLogin.class);
//                startActivity(intent);
            }
        });


    }

    private void OrgLogin() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!= null && user.isEmailVerified()){

            String userUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference Checkid= FirebaseDatabase.getInstance().getReference().child("Blocked").child("Organizer");
            Checkid.child(userUid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String state= snapshot.child("State").getValue().toString();

                        if (state.equals("False")){

                            Intent intent = new Intent(GuestScreen.this, TripOrganizerPostView.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(GuestScreen.this, "Already Logged In", Toast.LENGTH_SHORT).show();

                        }
                        else if (state.equals("True")){

                            Intent intent = new Intent(GuestScreen.this, BlockedScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            progressBar.setVisibility(View.INVISIBLE);
                            finish();

                        }
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }else{

            Intent intent=new Intent(GuestScreen.this, TripOrganizerLogin.class);
            startActivity(intent);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}