package com.example.escape.triporganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TripOrganizerLogin extends AppCompatActivity {

    TextView Register;
    EditText enterEmail,enterPassword;
    Button Login;
    ProgressBar progressBar;
    Toolbar toolbar;
    ImageView Back;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_organizer_login);

        mAuth=FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar_login);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Register=findViewById(R.id.registerText);
        enterEmail=findViewById(R.id.emailText);
        enterPassword=findViewById(R.id.passwordText);
        progressBar=findViewById(R.id.loginProgress);
        Back=findViewById(R.id.img_back);
        Login=findViewById(R.id.login);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TripOrganizerLogin.this, TripOrganigerRegister.class);
                startActivity(intent);
                finish();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTripOrganizer();
            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripOrganizerLogin.this, GuestScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loginTripOrganizer() {

        String email=enterEmail.getText().toString().trim();
        String password=enterPassword.getText().toString().trim();

        if (email.isEmpty()){
            enterEmail.setError("Field can't be empty");
            enterEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            enterEmail.setError("Please provide valid Email");
            enterEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            enterPassword.setError("Field can't be empty");
            enterPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            enterPassword.setError("Password length should be 6 character!");
            enterPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseAuth.getInstance().getCurrentUser().reload()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()){

                                Intent intent=new Intent(TripOrganizerLogin.this, TripOrganizerPostView.class);
                                startActivity(intent);

                            }else{
//                                user.sendEmailVerification();
                                Toast.makeText(TripOrganizerLogin.this, "Check your Email for verification", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }else{

                    Toast.makeText(TripOrganizerLogin.this, "Failed to login! Servers problem", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if (user!= null && user.isEmailVerified()){

            Intent intent = new Intent(this, TripOrganizerPostView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(TripOrganizerLogin.this, GuestScreen.class);
        startActivity(intent);
        finish();
        return true;
    }
}