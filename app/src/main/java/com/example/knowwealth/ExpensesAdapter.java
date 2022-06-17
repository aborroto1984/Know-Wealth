package com.example.knowwealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
    ArrayList<String> budget;
    ArrayList<Integer> percent;
    ProgressBar expenseBar;
    Context context;

    // Getting user instance
    User user = LoginActivity.user;

    public ExpensesAdapter (Context ct, ArrayList<String> expense, ArrayList<String> amount, ArrayList<Integer> percentVal, ArrayList<String> budget, ProgressBar bar){
        this.name = expense;
        this.data = amount;
        this.budget = budget;
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
        if (!budget.get(position).equals("BUDGET: 0")){
            holder.budgetText.setText(budget.get(position));
        }

        String test = holder.budgetText.getText().toString();

        if (!test.isEmpty()){
            float amount = Float.parseFloat(holder.amount.getText().toString().replaceAll("[^\\d|.]", ""));
            float budget = Float.parseFloat(test.replaceAll("[^\\d|.]", ""));
            if (budget < amount){
                holder.budgetText.setTextColor(Color.parseColor("red"));
            }
        }

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView category, amount, budgetText;
        ProgressBar bar;

        public MyViewHolder( View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.expenseCategory);
            amount = itemView.findViewById(R.id.expenseAmount);
            budgetText = itemView.findViewById(R.id.budgetText);
            bar = itemView.findViewById(R.id.expenseProgressBar);
        }
    }


}


