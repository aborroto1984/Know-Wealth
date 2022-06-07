package com.example.knowwealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    ArrayList<Transaction> transactions;
    Context context;
    Boolean isTransaction;

    // Getting user instance
    User user = LoginActivity.user;

    public TransactionAdapter(Context ct, ArrayList<Transaction> transactions, Boolean isTransaction) {

        this.transactions = transactions;
        this.context = ct;
        this.isTransaction = isTransaction;
    }

    @NonNull
    @Override
    public TransactionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.account_transactions_row, parent, false);
        return new TransactionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(transactions.get(position).getName());
        holder.date.setText((transactions.get(position).getDate()));
        holder.amount.setText((transactions.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        TextView amount;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.trasactionName);
            date = itemView.findViewById(R.id.trasactionDate);
            amount = itemView.findViewById(R.id.trasactionAmount);
        }
    }
}

