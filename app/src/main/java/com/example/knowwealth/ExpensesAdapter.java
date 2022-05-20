package com.example.knowwealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.MyViewHolder> {

    ArrayList<String> name;
    ArrayList<String> data;
    ArrayList<Integer> percent;
    ProgressBar expenseBar;
    Context context;

    public ExpensesAdapter (Context ct, ArrayList<String> expense, ArrayList<String> amount, ArrayList<Integer> percentVal, ProgressBar bar){
        this.name = expense;
        this.data = amount;
        this.percent = percentVal;
        this.expenseBar = bar;
        this.context = ct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.expenses_rows, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.category.setText(name.get(position));
        holder.amount.setText(data.get(position));
        holder.bar.setProgress(percent.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView category, amount;
        ProgressBar bar;

        public MyViewHolder( View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.expenseCategory);
            amount = itemView.findViewById(R.id.expenseAmount);
            bar = itemView.findViewById(R.id.expenseProgressBar);
        }
    }


}


