package com.example.knowwealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<String> utilities;
    ArrayList<String> uDates;
    FloatingActionButton closeBtn;
    Context context;

    public RecyclerViewAdapter (Context ct, ArrayList<String> utilities, ArrayList<String> uDates, FloatingActionButton closeBtn){
        this.utilities = utilities;
        this.uDates = uDates;
        this.closeBtn = closeBtn;
        this.context = ct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lists_rows, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text1.setText(utilities.get(position));
        holder.text2.setText(uDates.get(position));
        holder.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utilities.remove(position);
                uDates.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return utilities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text1, text2;
        FloatingActionButton closeBtn;

        public MyViewHolder( View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.name_type);
            text2 = itemView.findViewById(R.id.amount_date);
            closeBtn = itemView.findViewById(R.id.closeButton);
        }
    }
}
