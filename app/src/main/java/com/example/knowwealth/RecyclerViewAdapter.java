package com.example.knowwealth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<String> utilities;
    ArrayList<String> dueDates;
    Button closeBtn;
    Context context;

    public RecyclerViewAdapter (Context ct, ArrayList<String> utilities, ArrayList<String> dueDates){//, Button closeBtn){
        this.utilities = utilities;
        this.dueDates = dueDates;
        //this.closeBtn = closeBtn;
        this.context = ct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lists_rows, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.text1.setText(utilities.get(position));
        holder.text2.setText(dueDates.get(position));
    }

    @Override
    public int getItemCount() {
        return utilities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text1, text2;
        //Button closeBtn;

        public MyViewHolder( View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.name_type);
            text2 = itemView.findViewById(R.id.amount_date);
           // closeBtn = itemView.findViewById(R.id.closeButton);
        }
    }
}
