package com.akshaychavan.bankingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshaychavan.bankingapp.R;
import com.akshaychavan.bankingapp.UserSchema;
import com.akshaychavan.bankingapp.utility.DBAccess;
import com.akshaychavan.bankingapp.utility.GlobalCode;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Akshay Chavan on 05,November,2021
 * akshaychavan.kkwedu@gmail.com
 * BitTerrain Developers
 * https://www.linkedin.com/in/akshaychavan7/
 */
public class CustomersRowAdapter extends RecyclerView.Adapter<CustomersRowAdapter.DetailsViewHolder> {

    final String TAG = "CustomersRowAdapter";
    DBAccess dbAccess;
    GlobalCode globalCode;

    private ArrayList<UserSchema> mUserDetailsList;
    private Context mContext;

    public CustomersRowAdapter(ArrayList<UserSchema> userDetailsList, Context context) {
        mUserDetailsList = userDetailsList;
        this.mContext = context;
        dbAccess = DBAccess.getInstance(mContext);
        globalCode  = GlobalCode.getInstance(mContext);
    }


    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userslist_row_layout, parent, false);
        DetailsViewHolder holdingsViewHolder = new DetailsViewHolder(v, mContext);
        return holdingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {

        UserSchema currentItem = mUserDetailsList.get(position);

        holder.tvUserName.setText(currentItem.getName());
        String accountNo = "BK";
        for (int i = 0; i < (4 - currentItem.getId().toString().length()); i++) {
            accountNo += "0";
        }
        accountNo += currentItem.getId();
        holder.tvAccountNo.setText(accountNo);
        holder.tvUserName.setText(currentItem.getName());
        holder.tvBalanceAmount.setText("$"+String.format("%,d", currentItem.getBalance())+".00");

        holder.llUserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalCode.setSelectedCustomerID(currentItem.getId());
                globalCode.fetchTransactionsForUser();

                globalCode.getTvCustomerName().setText(currentItem.getName());
                globalCode.getTvCurrentBalance().setText("$"+String.format("%,d", currentItem.getBalance())+".00");


                String accountNo = "BK";
                for (int i = 0; i < (4 - currentItem.getId().toString().length()); i++) {
                    accountNo += "0";
                }
                accountNo += currentItem.getId();

                globalCode.getTvCustomerID().setText(accountNo);
                globalCode.getTvEmail().setText(currentItem.getEmail());
                globalCode.getTvTodaysDate().setText("Value as of "+globalCode.todaysDate());


                globalCode.getFmCustomerList().setVisibility(View.GONE);
                globalCode.getFmCustomerDetails().setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUserDetailsList.size();
    }


    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvAccountNo, tvBalanceAmount;
        LinearLayout llUserCard;

        public DetailsViewHolder(@NonNull View itemView, Context mContext) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvAccountNo = itemView.findViewById(R.id.tv_account_no);
            tvBalanceAmount = itemView.findViewById(R.id.tv_balance_amount);
            llUserCard = itemView.findViewById(R.id.ll_user_card);
        }
    }
}

