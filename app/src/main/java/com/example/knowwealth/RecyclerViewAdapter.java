package com.example.knowwealth;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.Strings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<String> utilities;
    ArrayList<String> dueDates;
    FloatingActionButton closeBtn;
    Context context;

    public RecyclerViewAdapter (Context ct, ArrayList<String> utilities, ArrayList<String> dueDates, FloatingActionButton closeBtn){
        this.utilities = utilities;
        this.dueDates = dueDates;
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
        holder.text2.setText(dueDates.get(position));
        holder.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.deleteFromList(dueDates.get(position),utilities.get(position));
                utilities.remove(position);
                dueDates.remove(position);
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

            //checks active activity to adjust layout of view
            String curAct = context.getClass().getName();
            if(curAct.equals("com.example.knowwealth.UtilityProcessing")){
                text1.setPadding(0,0,32,0);
                text2.setPadding(0,0,64,0);
                closeBtn.setVisibility(View.VISIBLE);
                closeBtn.setPadding(0,0,32,0);

            }
        }
    }
}
