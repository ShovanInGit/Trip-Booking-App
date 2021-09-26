package com.example.escape.triporganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.escape.GuestScreen;
import com.example.escape.ItemClickListener;
import com.example.escape.R;
import com.example.escape.model.OrganizerPost;
import com.example.escape.viewholder.OrganizerPostHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;

public class TripOrganizerPostView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView EmptyText;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    RecyclerView recyclerView;

    FirebaseRecyclerOptions<OrganizerPost> options;
    FirebaseRecyclerAdapter<OrganizerPost, OrganizerPostHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference blogPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_organizer_post_view);

        EmptyText=findViewById(R.id.empty_text);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_event);

        recyclerView = findViewById(R.id.post_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();

        UpdateNavHeader();

    }
    @Override
    protected void onStart() {
        super.onStart();

        String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        blogPost = database.getReference("Blog Post").child(CurrentUid);

        blogPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    options = new FirebaseRecyclerOptions.Builder<OrganizerPost>().setQuery(blogPost, OrganizerPost.class).build();
                    adapter = new FirebaseRecyclerAdapter<OrganizerPost, OrganizerPostHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull final OrganizerPostHolder holder, final int position, @NonNull final OrganizerPost model) {

                            holder.listname.setText(model.getTitle());
                            Picasso.get().load(model.getPostImage()).into(holder.listimage);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    String CurrentImage=model.getPostImage();
                                    String mKey=adapter.getRef(position).getKey();

                                    Intent intent=new Intent(TripOrganizerPostView.this,OrganizerPostDesc.class);
                                    intent.putExtra("ItemKey",mKey);
                                    intent.putExtra("Currimage",CurrentImage);
                                    startActivity(intent);
                                }
                            });
//                  holder.setItemClickListener(new ItemClickListener() {
//                      @Override
//                      public void onItemClick(View v, int position, boolean isLongClick) {
//
//                          String CurrentImage=model.getPostImage();
//                          String mKey=adapter.getRef(position).getKey();
//
////                          BitmapDrawable bitmapDrawable=(BitmapDrawable)holder.listimage.getDrawable();
////                          Bitmap bitmap=bitmapDrawable.getBitmap();
////                          ByteArrayOutputStream stream=new ByteArrayOutputStream();
////                          bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
////                          byte[] Image= stream.toByteArray();
////
//                          Intent intent=new Intent(TripOrganizerPostView.this,OrganizerPostDesc.class);
//                          intent.putExtra("ItemKey",mKey);
//                          intent.putExtra("Currimage",CurrentImage);
//                          startActivity(intent);
//
//                      }
//                  });

                        }

                        @NonNull
                        @Override
                        public OrganizerPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_item, parent, false);
                            return new OrganizerPostHolder(view);
                        }
                    };

                    adapter.startListening();
                    recyclerView.setAdapter(adapter);

                    recyclerView.setVisibility(View.VISIBLE);
                    EmptyText.setVisibility(View.INVISIBLE);

                }else{

                    recyclerView.setVisibility(View.INVISIBLE);
                    EmptyText.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_event) {

            Intent intent = new Intent(TripOrganizerPostView.this, TripOrganizerPostView.class);
            startActivity(intent);
            finish();

        }
        if (item.getItemId() == R.id.nav_post) {

            Intent intent = new Intent(TripOrganizerPostView.this, AddPostActivity.class);
            startActivity(intent);
            finish();

        }
        if (item.getItemId() == R.id.nav_logout) {

            TripOrganizerLogout();

        }
        if (item.getItemId() == R.id.nav_profile) {

            Intent intent = new Intent(TripOrganizerPostView.this, UpdateProfile.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.nav_booking) {

            Intent intent = new Intent(TripOrganizerPostView.this, ShowBookingPost.class);
            startActivity(intent);
            finish();

        }
        if (item.getItemId() == R.id.nav_aboutus) {

            Intent intent = new Intent(TripOrganizerPostView.this, AboutInfo.class);
            startActivity(intent);
            finish();

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.side_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.nav_booking);
        View actionView = menuItem.getActionView();

        final TextView Counter =actionView.findViewById(R.id.badge_counter);


        String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference bookPost = database.getReference("BookedPost").child("TripOrgView");

        Query query = bookPost.orderByChild("OrganizerUid").equalTo(CurrentUid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                   long totalItem = snapshot.getChildrenCount();
                    Counter.setText(String.valueOf(totalItem));
                    Counter.setVisibility(View.VISIBLE);

                }else{
                    Counter.setVisibility(View.INVISIBLE);

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_booking) {

            Intent intent = new Intent(TripOrganizerPostView.this, ShowBookingPost.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    public void UpdateNavHeader(){

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        final TextView NavUserName=headerView.findViewById(R.id.username);
        final ImageView NavUserImg=headerView.findViewById(R.id.user_img);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference showHeaderName = FirebaseDatabase.getInstance().getReference().child("TripOrganizer").child(uid);

        showHeaderName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    if (snapshot.child("image").exists()) {

                        String name = snapshot.child("fullname").getValue().toString();
                        NavUserName.setText(name);
                        String img = snapshot.child("image").getValue().toString();
                        Picasso.get().load(img).into(NavUserImg);


                    }else{
                        String name = snapshot.child("fullname").getValue().toString();
                        NavUserName.setText(name);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void TripOrganizerLogout() {

        AlertDialog.Builder Builder = new AlertDialog.Builder(TripOrganizerPostView.this);
        Builder.setTitle("Logout");
        Builder
                .setMessage("You sure you want to Logout?")
                .setCancelable(false)
                .setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity

                        FirebaseAuth.getInstance().signOut();

                        Intent intent = new Intent(TripOrganizerPostView.this, GuestScreen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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


}
