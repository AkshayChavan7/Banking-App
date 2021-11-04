package com.akshaychavan.bankingapp.utility;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Akshay Chavan on 03,November,2021
 * akshaychavan.kkwedu@gmail.com
 * BitTerrain Developers
 * https://www.linkedin.com/in/akshaychavan7/
 */
public class DBHelper extends SQLiteAssetHelper {

    final String TAG = "DBHelper";
    public static final String DATABASE_NAME = "bankingAppDB.db";
    public static final int DATABASE_VERSION = 10;
    public static String DATABASE_PATH = null;
    private Context context;

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//        this.context = context;
//        DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
//        Log.e(TAG, "Path:"+DATABASE_PATH);
    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//
//    public void transferAmount(int fromAccountID, int toAccountID, int amount) {
//        SQLiteDatabase db = this.getWritableDatabase();
////        ContentValues cv = new ContentValues();
//
//        // update user table with deducted and credited amounts
//        String deductAmountQuery = "UPDATE user SET balance=(SELECT balance FROM user WHERE ID=" + fromAccountID + ")-" + amount + " WHERE ID = " + fromAccountID + ";";
//        String creditAmountQuery = "UPDATE user SET balance=(SELECT balance FROM user WHERE ID=" + toAccountID + ")-" + amount + " WHERE ID = " + toAccountID + ";";
//        db.execSQL(deductAmountQuery);
//        db.execSQL(creditAmountQuery);
//
//        // add entry into transfers table
//        String transferRecordQuery = "INSERT INTO transfers VALUES(" + fromAccountID + "," + toAccountID + "," + amount + ", datetime());";
//        db.execSQL(transferRecordQuery);
//        db.close();
//    }
//
//
//    public Cursor fetchAllUsersData() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT *  FROM user;";
//
//        Cursor cursor = null;
//
//        if (db != null) {
//            cursor = db.rawQuery(query, null);
//        }
//        db.close();
//        return cursor;
//    }
//
//
//    public void checkDatabase() {
//        String fullPath = DATABASE_PATH + DATABASE_NAME;
//        SQLiteDatabase.openDatabase(fullPath, null, 0);
//    }

}
