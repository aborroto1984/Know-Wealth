package com.example.knowwealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BudgetEditAdapter extends RecyclerView.Adapter<BudgetEditAdapter.MyViewHolder>{
    static DatabaseReference userDatabase= User.userDatabase.child(User.getuID());

    User user;
    ArrayList<User.Budget> budgets;
    Context context;
    Button budgetEdit;
    Button budgetReset;
    Button budgetDone;
    ImageView budgetUndo;
    EditText budgetEditText;
    Boolean isExpanded;
    String oldBudget;


    public BudgetEditAdapter(Context ct, ArrayList<User.Budget> budgets, Button budgetEdit, Button budgetReset, Button budgetDone, ImageView budgetUndo, EditText budgetEditText) {
        this.budgetDone = budgetDone;
        this.budgetEdit = budgetEdit;
        this.budgetReset = budgetReset;
        this.context = ct;
        this.budgetUndo = budgetUndo;
        this.budgets = budgets;
        this.budgetEditText = budgetEditText;
        this.isExpanded = false;
        this.oldBudget = "0";
        this.user = LoginActivity.user;
    }

    @NonNull
    @Override
    public BudgetEditAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.budget_edit_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetEditAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(budgets.get(position).getName());
        holder.amount.setText(budgets.get(position).getAmount());
        holder.budgetEditExpanded.setVisibility( budgets.get(position).getIsExpanded() ? View.VISIBLE: View.GONE);

        //----------------------------------------------------------------------------------------------------------------  BUTTONS
        holder.budgetEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgets.get(position).setExpanded(!budgets.get(position).getIsExpanded());
                notifyDataSetChanged();
            }
        });

        holder.budgetDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldBudget = budgets.get(position).getAmount();
                if (!holder.budgetEditText.getText().toString().equals("")){
                    budgets.get(position).setAmount(formatCurrency(holder.budgetEditText.getText().toString()));
                    userDatabase.child("Budgets/" + budgets.get(position).getName()).setValue(formatCurrency(holder.budgetEditText.getText().toString()));
                }
                budgets.get(position).setExpanded(!budgets.get(position).getIsExpanded());

                InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),0);

                notifyDataSetChanged();
            }
        });

        holder.budgetReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgets.get(position).setAmount("0");
                DatabaseReference userDatabase= User.userDatabase.child(User.getuID());
                userDatabase.child("Budgets/" + budgets.get(position).getName()).setValue("0");
                budgets.get(position).setExpanded(!budgets.get(position).getIsExpanded());

                notifyDataSetChanged();
            }
        });

        holder.budgetUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgets.get(position).setAmount(oldBudget);
                userDatabase.child("Budgets/" + budgets.get(position).getName()).setValue(oldBudget);
                oldBudget = "0";
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() { return budgets.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button budgetEdit, budgetReset, budgetDone;
        ImageView budgetUndo;
        EditText budgetEditText;
        TextView name, amount;
        CardView budgetEditExpanded;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            budgetEdit = itemView.findViewById(R.id.budgetEditBtn);
            budgetReset = itemView.findViewById(R.id.budgetResetBtn);
            budgetDone = itemView.findViewById(R.id.budgetDoneBtn);
            budgetUndo = itemView.findViewById(R.id.budgetUndoBtn);
            budgetEditText = itemView.findViewById(R.id.budgetEditText);
            name = itemView.findViewById(R.id.budgetName);
            amount = itemView.findViewById(R.id.budgetAmount);
            budgetEditExpanded = itemView.findViewById(R.id.budgetEditExpanded);
        }
    }

    //----------------------------------------------------------------------------------------------------------------  HELPER METHODS

    // Currency formatter
    private static String formatCurrency(String number){
        DecimalFormat formatter = new DecimalFormat("$ ###,###,##0.00");
        return formatter.format(Float.parseFloat(number));
    }
}
