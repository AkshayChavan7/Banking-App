package com.akshaychavan.bankingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshaychavan.bankingapp.R;
import com.akshaychavan.bankingapp.UserSchema;
import com.akshaychavan.bankingapp.utility.DBAccess;
import com.akshaychavan.bankingapp.utility.GlobalCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay Chavan on 05,November,2021
 * akshaychavan.kkwedu@gmail.com
 * BitTerrain Developers
 * https://www.linkedin.com/in/akshaychavan7/
 */
public class CustomersRowAdapter extends RecyclerView.Adapter<CustomersRowAdapter.DetailsViewHolder> implements Filterable {

    final String TAG = "CustomersRowAdapter";
    DBAccess dbAccess;
    GlobalCode globalCode;
    private ArrayList<UserSchema> mUserDetailsList, mUserDetailsListFull;
    private Context mContext;


    public CustomersRowAdapter(ArrayList<UserSchema> userDetailsList, Context context) {
        mUserDetailsList = userDetailsList;
        mUserDetailsListFull = new ArrayList<>(mUserDetailsList);
        this.mContext = context;
        dbAccess = DBAccess.getInstance(mContext);
        globalCode = GlobalCode.getInstance(mContext);
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
        holder.tvBalanceAmount.setText("$" + String.format("%,d", currentItem.getBalance()) + ".00");

        holder.llUserCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalCode.setSelectedCustomerID(currentItem.getId());
                globalCode.fetchTransactionsForUser();

                globalCode.getTvCustomerName().setText(currentItem.getName());
                globalCode.getTvCurrentBalance().setText("$" + String.format("%,d", currentItem.getBalance()) + ".00");


                String accountNo = "BK";
                for (int i = 0; i < (4 - currentItem.getId().toString().length()); i++) {
                    accountNo += "0";
                }
                accountNo += currentItem.getId();

                globalCode.getTvCustomerID().setText(accountNo);
                globalCode.getTvEmail().setText(currentItem.getEmail());
                globalCode.getTvTodaysDate().setText("Value as of " + globalCode.todaysDate());


                globalCode.getFmCustomerList().setVisibility(View.GONE);
                globalCode.getFmCustomerDetails().setVisibility(View.VISIBLE);
                globalCode.getIvBack().setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUserDetailsList.size();
    }

    @Override
    public Filter getFilter() {
        return userSearchFilter;
    }

    public Filter userSearchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<UserSchema> usersFilteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {              // If empty search text
                usersFilteredList.addAll(mUserDetailsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                // filtering
                for (UserSchema user: mUserDetailsListFull) {
                    if (user.getName().toLowerCase().contains(filterPattern) || ("BK000"+user.getId()).toLowerCase().contains(filterPattern)) {
                        usersFilteredList.add(user);
                    }
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = usersFilteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mUserDetailsList.clear();
            mUserDetailsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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

