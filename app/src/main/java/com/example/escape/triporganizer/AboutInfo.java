package com.example.escape.triporganizer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.escape.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutInfo extends AppCompatActivity {

    TextView AboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_info);

        AboutUs=findViewById(R.id.about_us);

        AboutText();
    }
    private void AboutText() {

        final DatabaseReference aboutus = FirebaseDatabase.getInstance().getReference("About Us");
        aboutus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String text= snapshot.child("Text").getValue().toString();
                AboutUs.setText(text);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AboutInfo.this, TripOrganizerPostView.class);
        startActivity(intent);
        finish();
    }
}