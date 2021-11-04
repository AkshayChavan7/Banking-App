package com.akshaychavan.bankingapp.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Akshay Chavan on 04,November,2021
 * akshaychavan.kkwedu@gmail.com
 * BitTerrain Developers
 * https://www.linkedin.com/in/akshaychavan7/
 */
public class DBAccess {

    private final String TAG = "DBAccess";
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DBAccess instance;
    Cursor c = null;

    private DBAccess(Context context) {
        this.openHelper = new DBHelper(context);
    }

    public static DBAccess getInstance(Context context) {
        if(instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    public void openConnection() {
        this.db = openHelper.getWritableDatabase();
    }

    public void closeConnection() {
        if(this.db!=null) {
            this.db.close();
        }
    }

    public void transferAmount(int fromAccountID, int toAccountID, int amount) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();

//        openConnection();
        // update user table with deducted and credited amounts
        String deductAmountQuery = "UPDATE user SET balance=(SELECT balance FROM user WHERE ID=" + fromAccountID + ")-" + amount + " WHERE ID = " + fromAccountID + ";";
        String creditAmountQuery = "UPDATE user SET balance=(SELECT balance FROM user WHERE ID=" + toAccountID + ")+" + amount + " WHERE ID = " + toAccountID + ";";
        this.db.execSQL(deductAmountQuery);
        this.db.execSQL(creditAmountQuery);

        // add entry into transfers table
        String transferRecordQuery = "INSERT INTO transfers VALUES(" + fromAccountID + "," + toAccountID + "," + amount + ", datetime());";
        this.db.execSQL(transferRecordQuery);
//        this.db.close();
//        closeConnection();
    }


    public void printAllTables() {
        openConnection();
        Cursor c = this.db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' order by name", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
//                Toast.makeText(activityName.this, "Table Name=> "+c.getString(0), Toast.LENGTH_LONG).show();
                Log.e(TAG, "Table Name==>"+c.getString(0));
                c.moveToNext();
            }
        }
        closeConnection();
    }


    public Cursor fetchAllUsersData() {
//        db = openHelper.getReadableDatabase();

//        Log.e(TAG, "Path:"+this.db.getPath());
//        openConnection();
        String query = "SELECT *  FROM user;";

        Cursor cursor = null;

        if (db != null) {
            cursor = this.db.rawQuery(query, null);
        }
//        db.close();
//        closeConnection();
        return cursor;
    }



}
