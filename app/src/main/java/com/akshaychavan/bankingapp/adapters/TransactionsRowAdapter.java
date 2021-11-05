package com.akshaychavan.bankingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.akshaychavan.bankingapp.R;
import com.akshaychavan.bankingapp.TransferSchema;
import com.akshaychavan.bankingapp.utility.DBAccess;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Akshay Chavan on 09,May,2021
 * akshaychavan.kkwedu@gmail.com
 */
public class TransactionsRowAdapter extends RecyclerView.Adapter<TransactionsRowAdapter.DetailsViewHolder> {

    final String TAG = "TransactionsRowAdapter";
    DBAccess dbAccess;
    private ArrayList<TransferSchema> mTransferDetailsList;
    private Context mContext;
    final int min = 1000000000;
    final int max = 1111111111;
    String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    public TransactionsRowAdapter(ArrayList<TransferSchema> transferDetailsList, Context context) {
        mTransferDetailsList = transferDetailsList;
        this.mContext = context;
        dbAccess = DBAccess.getInstance(mContext);
    }


    @NonNull
    @Override
    public DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_row_layout, parent, false);
        DetailsViewHolder holdingsViewHolder = new DetailsViewHolder(v, mContext);
        return holdingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, int position) {
        final int random = new Random().nextInt((max - min) + 1) + min;

        TransferSchema currentItem = mTransferDetailsList.get(position);

        dbAccess.openConnection();
        holder.tvBeneficiaryName.setText(dbAccess.getUserNameFromID(currentItem.getToID()));
        dbAccess.closeConnection();

        holder.tvTransactionID.setText("UPI-"+random);

        String timestamps[] = currentItem.getTimestamp().split(" ")[0].split("-");          // timestamp format- 2021-11-04 06:36:51
        holder.tvDay.setText(timestamps[2]);
        int monthIndex = Integer.parseInt(timestamps[1]) -1;
;        holder.tvMonthAndYear.setText(months[monthIndex]+"'"+timestamps[0].substring(2,4));

        // event for calendar click
        holder.cvCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Transaction on "+timestamps[2]+" "+months[monthIndex]+", "+timestamps[0]+" at "+currentItem.getTimestamp().split(" ")[1], Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTransferDetailsList.size();
    }


    public static class DetailsViewHolder extends RecyclerView.ViewHolder {
        TextView tvBeneficiaryName, tvTransactionID, tvDay, tvMonthAndYear;
        CardView cvCalender;

        public DetailsViewHolder(@NonNull View itemView, Context mContext) {
            super(itemView);
            tvBeneficiaryName = itemView.findViewById(R.id.tv_beneficiary_name);
            tvTransactionID = itemView.findViewById(R.id.tv_transaction_id);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvMonthAndYear = itemView.findViewById(R.id.tv_month_and_year);
            cvCalender = itemView.findViewById(R.id.cv_calendar);
        }
    }
}
