package com.akshaychavan.bankingapp.utility;

import android.content.Context;
import android.database.Cursor;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.akshaychavan.bankingapp.MainActivity;
import com.akshaychavan.bankingapp.TransferSchema;
import com.akshaychavan.bankingapp.adapters.TransactionsRowAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Akshay Chavan on 05,November,2021
 * akshaychavan.kkwedu@gmail.com
 * BitTerrain Developers
 * https://www.linkedin.com/in/akshaychavan7/
 */
public class GlobalCode {

    private static GlobalCode mInstance = null;
    private final String TAG = "GlobalCode";
    public int selectedCustomerID;
    public String selectedPage = "customers_list";
    FrameLayout fmCustomerList, fmCustomerDetails;
    TextView tvCustomerName, tvCurrentBalance, tvCustomerID, tvEmail, tvTodaysDate;
    DBAccess dbAccess;
    ArrayList<TransferSchema> transferSchemaList = new ArrayList<>();
    // Tranasactions List Adapter
    RecyclerView transactionsListRecylcer;
    RecyclerView.LayoutManager transactionsListLayoutManager;
    RecyclerView.Adapter transactionsListAdapter;
    /////////////////////////////////

    Context mContext;

    public GlobalCode(Context context) {
        mContext = context;
        dbAccess = DBAccess.getInstance(context);
    }

    public static synchronized GlobalCode getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new GlobalCode(context);
        }
        return mInstance;
    }

    public void fetchTransactionsForUser() {
        // fetching transactions for particular user
        dbAccess.openConnection();
        Cursor cursor = null;
        cursor = dbAccess.fetchTransactionsForUser(selectedCustomerID);
        transferSchemaList.clear();
        while (cursor.moveToNext()) {
//            Log.e(TAG,cursor.getString(1));
            transferSchemaList.add(new TransferSchema(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3)));
        }
        dbAccess.closeConnection();

        transactionsListAdapter = new TransactionsRowAdapter(transferSchemaList, mContext);
        transactionsListRecylcer.setLayoutManager(transactionsListLayoutManager);
        transactionsListRecylcer.setAdapter(transactionsListAdapter);
    }

    public String todaysDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String todayDate = df.format(c);

        return todayDate;
    }

    public int getSelectedCustomerID() {
        return selectedCustomerID;
    }

    public void setSelectedCustomerID(int selectedCustomerID) {
        this.selectedCustomerID = selectedCustomerID;
    }

    public String getSelectedPage() {
        return selectedPage;
    }

    public void setSelectedPage(String selectedPage) {
        this.selectedPage = selectedPage;
    }

    public FrameLayout getFmCustomerList() {
        return fmCustomerList;
    }

    public void setFmCustomerList(FrameLayout fmCustomerList) {
        this.fmCustomerList = fmCustomerList;
    }

    public FrameLayout getFmCustomerDetails() {
        return fmCustomerDetails;
    }

    public void setFmCustomerDetails(FrameLayout fmCustomerDetails) {
        this.fmCustomerDetails = fmCustomerDetails;
    }

    public RecyclerView getTransactionsListRecylcer() {
        return transactionsListRecylcer;
    }

    public void setTransactionsListRecylcer(RecyclerView transactionsListRecylcer) {
        this.transactionsListRecylcer = transactionsListRecylcer;
    }

    public RecyclerView.LayoutManager getTransactionsListLayoutManager() {
        return transactionsListLayoutManager;
    }

    public void setTransactionsListLayoutManager(RecyclerView.LayoutManager transactionsListLayoutManager) {
        this.transactionsListLayoutManager = transactionsListLayoutManager;
    }

    public RecyclerView.Adapter getTransactionsListAdapter() {
        return transactionsListAdapter;
    }

    public void setTransactionsListAdapter(RecyclerView.Adapter transactionsListAdapter) {
        this.transactionsListAdapter = transactionsListAdapter;
    }

    public TextView getTvCustomerName() {
        return tvCustomerName;
    }

    public void setTvCustomerName(TextView tvCustomerName) {
        this.tvCustomerName = tvCustomerName;
    }

    public TextView getTvCurrentBalance() {
        return tvCurrentBalance;
    }

    public void setTvCurrentBalance(TextView tvCurrentBalance) {
        this.tvCurrentBalance = tvCurrentBalance;
    }

    public TextView getTvCustomerID() {
        return tvCustomerID;
    }

    public void setTvCustomerID(TextView tvCustomerID) {
        this.tvCustomerID = tvCustomerID;
    }

    public TextView getTvEmail() {
        return tvEmail;
    }

    public void setTvEmail(TextView tvEmail) {
        this.tvEmail = tvEmail;
    }

    public TextView getTvTodaysDate() {
        return tvTodaysDate;
    }

    public void setTvTodaysDate(TextView tvTodaysDate) {
        this.tvTodaysDate = tvTodaysDate;
    }
}
