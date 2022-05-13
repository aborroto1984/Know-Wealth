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

    ArrayList<String> name;
    ArrayList<String> data;
    FloatingActionButton closeBtn;
    Context context;

    public RecyclerViewAdapter (Context ct, ArrayList<String> utilities, ArrayList<String> uDates, FloatingActionButton closeBtn){
        this.name = utilities;
        this.data = uDates;
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
        holder.text1.setText(name.get(position));
        holder.text2.setText(data.get(position));
        holder.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (User.currentActivity){
                    case "utility":
                        User.deleteFromList(data.get(position), name.get(position), User.utilities);
                        break;
                    case "subscriptions":
                        User.deleteFromList(data.get(position), name.get(position), User.subscriptions);
                        break;
                    case "creditCards":
                        User.deleteFromList(data.get(position), name.get(position), User.creditCards);
                        break;
                    case "expenses":
                        User.deleteFromList(data.get(position), name.get(position), User.expenses);
                        break;


                }
                name.remove(position);
                data.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return name.size();
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
