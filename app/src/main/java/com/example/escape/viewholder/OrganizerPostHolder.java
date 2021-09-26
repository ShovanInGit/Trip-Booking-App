package com.example.escape.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.escape.ItemClickListener;
import com.example.escape.R;

public class OrganizerPostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView listname;
    public ImageView listimage;

    ItemClickListener itemClickListener;

    public OrganizerPostHolder(@NonNull View itemView) {
        super(itemView);

        listname = itemView.findViewById(R.id.list_name);
        listimage = itemView.findViewById(R.id.list_image);

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
