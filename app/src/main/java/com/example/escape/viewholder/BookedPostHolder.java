package com.example.escape.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.escape.ItemClickListener;
import com.example.escape.R;

public class BookedPostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView ListTitle,ListBook;
    ItemClickListener itemClickListener;


    public BookedPostHolder(@NonNull View itemView) {
        super(itemView);

        ListTitle=itemView.findViewById(R.id.list_title);
        ListBook=itemView.findViewById(R.id.bookseat);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick(v,getLayoutPosition(),false);

    }
    public  void setItemClickListener(ItemClickListener ic){
        this.itemClickListener=ic;
    }
}
