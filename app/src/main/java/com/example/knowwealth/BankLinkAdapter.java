package com.example.knowwealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BankLinkAdapter  extends RecyclerView.Adapter<BankLinkAdapter.MyViewHolder> {

    ArrayList<BankAccount> accounts;
    Context context;
    CheckBox selected;

    // Getting user instance
    User user = LoginActivity.user;

    public BankLinkAdapter (Context ct, ArrayList<BankAccount> accounts, CheckBox selected){

        this.accounts = accounts;
        this.context = ct;
        this.selected = selected;
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
        holder.name.setText(accounts.get(position).getName());
        holder.type.setText(accounts.get(position).getType());
        holder.balance.setText(accounts.get(position).getBalance());
//         if (holder.selected.isChecked()){
//             accounts.get(position).setSelected(true);
//         }
        holder.selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accounts.get(position).setSelected(holder.selected.isChecked());
            }
        });

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

        public MyViewHolder( View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.accountName);
            type = itemView.findViewById(R.id.accountDescript);
            balance = itemView.findViewById(R.id.accountBalance);
            selected = itemView.findViewById(R.id.accountSelect);
        }
    }
}
