package com.example.escape.triporganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.escape.ItemClickListener;
import com.example.escape.R;
import com.example.escape.model.BookedPost;
import com.example.escape.viewholder.BookedPostHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ShowBookingPost extends AppCompatActivity {

    TextView FormalText;
    RecyclerView BookedList;
    FirebaseDatabase database;
    Toolbar toolbar;

    FirebaseRecyclerOptions<BookedPost> options;
    FirebaseRecyclerAdapter<BookedPost, BookedPostHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_booking_post);

        database = FirebaseDatabase.getInstance();
        FormalText=findViewById(R.id.formal_text);

        toolbar = findViewById(R.id.toolbar_book);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BookedList=findViewById(R.id.booked_list);
        BookedList.setHasFixedSize(true);
        BookedList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        final String CurrentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference bookPost = database.getReference("BookedPost").child("TripOrgView");

        Query query = bookPost.orderByChild("OrganizerUid").equalTo(CurrentUid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    options = new FirebaseRecyclerOptions.Builder<BookedPost>()
                            .setQuery(bookPost.orderByChild("OrganizerUid").equalTo(CurrentUid), BookedPost.class).build();
                    adapter= new FirebaseRecyclerAdapter<BookedPost, BookedPostHolder>(options) {
                        @NonNull
                        @Override
                        public BookedPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
                            return new BookedPostHolder(view);
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull BookedPostHolder holder, final int position, @NonNull final BookedPost model) {

                            holder.ListTitle.setText(model.getPostTitle());
                            holder.ListBook.setText(model.getBookingSeat());

//                            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent ConfirmPost=new Intent(ShowBookingPost.this,ConfirmBooking.class);
//                                    ConfirmPost.putExtra("BookKey",adapter.getRef(position).getKey());
//                                    startActivity(ConfirmPost);
//                                }
//                            });

                            holder.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position, boolean isLongClick) {

                                    Intent ConfirmPost=new Intent(ShowBookingPost.this,ConfirmBooking.class);
                                    ConfirmPost.putExtra("BookKey",adapter.getRef(position).getKey());
                                    startActivity(ConfirmPost);
                                }
                            });

                        }
                    };
                    adapter.startListening();
                    BookedList.setAdapter(adapter);
                    BookedList.setVisibility(View.VISIBLE);
                    FormalText.setVisibility(View.INVISIBLE);

                }else{

                    BookedList.setVisibility(View.INVISIBLE);
                    FormalText.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(ShowBookingPost.this, TripOrganizerPostView.class);
        startActivity(intent);
        finish();
        return true;
    }
}