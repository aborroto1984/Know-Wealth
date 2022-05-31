package com.example.knowwealth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    ArrayList<String> name;
    ArrayList<String> data;
    ArrayList<String> paid;
    FloatingActionButton closeBtn;
    MaterialCheckBox checkBox;
    Context context;

    public RecyclerViewAdapter (Context ct, ArrayList<String> utilities, ArrayList<String> uDates, ArrayList<String> paid, FloatingActionButton closeBtn, MaterialCheckBox checkBox){
        this.name = utilities;
        this.data = uDates;
        this.paid = paid;
        this.closeBtn = closeBtn;
        this.checkBox = checkBox;
        this.context = ct;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_rows, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text1.setText(name.get(position));

        String curAct = context.getClass().getName();
        if(curAct.equals("com.example.knowwealth.DashBoard")){
            holder.text2.setVisibility(View.GONE);
            holder.dashExpenseText.setText(data.get(position));
            holder.dashExpenseText.setVisibility(View.VISIBLE);
        }else if(curAct.equals("com.example.knowwealth.DueDatesCalendar")){
            Boolean checkPaid;
            if(paid.get(position).equals("true")){
                checkPaid = true;
            }else{
                checkPaid = false;
            }
            holder.checkBox.setChecked(checkPaid);
        }else{
            holder.text2.setText(data.get(position));
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempName;
                String tempData;
                String localName;
                String localData;
                String udpatePaid;

                if(paid.get(position).equals("true")){
                    udpatePaid = "false";
                    paid.set(position, "false");
                }else{
                    udpatePaid = "true";
                    paid.set(position, "true");
                }
                localData = data.get(position);
                localName = name.get(position);
                if(User.utilities.size() > 0) {
                    for (int i = 0; i < User.utilities.size(); i++) {
                        User.UtilDate temp = User.utilities.get(i);
                        tempName = temp.getName();
                        tempData = temp.getData();
                        if (tempName.equals(localName)){
                            if(tempData.equals(localData)){
                                User.updatedPaid(localData,localName,udpatePaid, "Utilities",User.utilities);
                                onBindViewHolder(holder,position);
                                return;
                            }
                        }
                    }
                }
                if(User.subscriptions.size() > 0) {
                    for (int i = 0; i < User.subscriptions.size(); i++) {
                        User.UtilDate temp = User.subscriptions.get(i);
                        tempName = temp.getName();
                        tempData = temp.getData();
                        if (tempName.equals(localName)){
                            if(tempData.equals(localData)){
                                User.updatedPaid(localData,localName,udpatePaid, "Subscriptions", User.subscriptions);
                                onBindViewHolder(holder,position);
                                return;
                            }
                        }
                    }
                }
                if(User.creditCards.size() > 0) {
                    for (int i = 0; i < User.creditCards.size(); i++) {
                        User.UtilDate temp = User.creditCards.get(i);
                        tempName = temp.getName();
                        tempData = temp.getData();
                        if (tempName.equals(localName)){
                            if(tempData.equals(localData)){
                                User.updatedPaid(localData,localName,udpatePaid, "Credit Cards", User.creditCards);
                                onBindViewHolder(holder,position);
                                return;
                            }
                        }
                    }
                }
            }
        });

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
                        String amountNum = data.get(position).replaceAll("[$,,]", "");
                        User.deleteExpenseFromList(amountNum, name.get(position), User.expenses);
                        break;
                    case "budgets":
                        User.deleteBudget(name.get(position), User.expenses);
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
        TextView text1, text2, dashExpenseText;
        FloatingActionButton closeBtn;
        MaterialCheckBox checkBox;

        public MyViewHolder( View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.name_type);
            text2 = itemView.findViewById(R.id.amount_date);
            dashExpenseText = itemView.findViewById(R.id.dashExpenseText);

            closeBtn = itemView.findViewById(R.id.closeButton);
            checkBox = itemView.findViewById(R.id.checkBox);

            //checks active activity to adjust layout of lists
            String curAct = context.getClass().getName();
            if(curAct.equals("com.example.knowwealth.ProcessingScreens")){
                closeBtn.setVisibility(View.VISIBLE);
                closeBtn.setPadding(0,0,45,0);
            }else if(curAct.equals("com.example.knowwealth.DueDatesCalendar")){
                text1.setPadding(70,0,0,0);
                text2.setVisibility(View.GONE);
                checkBox.setVisibility(View.VISIBLE);
            }else if(curAct.equals("com.example.knowwealth.DashBoard")){
                text2.setPadding(70,0,0,0);
            }
        }
    }
}
