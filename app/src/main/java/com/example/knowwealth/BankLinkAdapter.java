package com.example.knowwealth;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BankLinkAdapter  extends RecyclerView.Adapter<BankLinkAdapter.MyViewHolder> {

    ArrayList<BankAccount> accounts;
    Context context;
    CheckBox selected;
    Boolean isTransaction;
    RecyclerView transactionsList;

    // Getting user instance
    User user = LoginActivity.user;

    public BankLinkAdapter (Context ct, ArrayList<BankAccount> accounts, RecyclerView transactionsList, CheckBox selected, Boolean isTransaction){

        this.accounts = accounts;
        this.context = ct;
        this.selected = selected;
        this.isTransaction = isTransaction;
        this.transactionsList = transactionsList;
    }

    @NonNull
    @Override
    public BankLinkAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.accounts_rows, parent, false);
        return new BankLinkAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankLinkAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TransactionAdapter transAdapter = new TransactionAdapter(context, accounts.get(position).transactions, true);
        LinearLayoutManager transactionLayoutManager = new LinearLayoutManager(context);
        holder.transactionList.setAdapter(transAdapter);
        holder.transactionList.setLayoutManager(transactionLayoutManager);

        String checkBoxShow = accounts.get(position).getType();
        if (checkBoxShow.equals("Checking") || checkBoxShow.equals("Credit card")){ holder.selected.setVisibility(View.VISIBLE); }
        else{ holder.selected.setVisibility(View.GONE);}

        Boolean show = accounts.get(position).getShow();
        holder.title.setVisibility( show ? View.VISIBLE: View.GONE);

        holder.name.setText(accounts.get(position).getName());
        holder.type.setText(accounts.get(position).getType());
        holder.balance.setText(accounts.get(position).getBalance());
        holder.selected.setChecked(accounts.get(position).getSelected());
        holder.selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accounts.get(position).setSelected(holder.selected.isChecked());
            }
        });

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accounts.get(position).setExpanded(!accounts.get(position).getExpanded());
                notifyItemChanged(position);
            }
        });
        boolean isExpanded = accounts.get(position).getExpanded();
        holder.transactionList.setVisibility( isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView type;
        TextView balance;
        CheckBox selected;
        RecyclerView transactionList;
        CardView title;
        TextView showAllAccounts;

        public MyViewHolder( View itemView) {
            super(itemView);

            transactionList = itemView.findViewById(R.id.accountTrasactions);
            name = itemView.findViewById(R.id.accountName);
            type = itemView.findViewById(R.id.accountDescript);
            balance = itemView.findViewById(R.id.accountBalance);
            selected = itemView.findViewById(R.id.accountSelect);
            title = itemView.findViewById(R.id.accountTitle);

        }
    }
}
