package com.akshaychavan.bankingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshaychavan.bankingapp.adapters.CustomersRowAdapter;
import com.akshaychavan.bankingapp.utility.DBAccess;
import com.akshaychavan.bankingapp.utility.GlobalCode;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

/**
 * Created by Akshay Chavan on 04,November,2021
 * akshaychavan.kkwedu@gmail.com
 * BitTerrain Developers
 * https://www.linkedin.com/in/akshaychavan7/
 */
public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";
    boolean doubleBackToExitPressedOnce = false;
    public static final String SHARED_PREFS = "sharedprefs";
    public static final String LOGIN_STATE = "login";

    DBAccess dbAccess;
    GlobalCode globalCode;
    NavigationView navigationView;
    Toolbar toolbar;
    FrameLayout fmTransferAmount;
    TextView tvUserName, tvUserEmail, tvMadeBy;
    ImageView ivProfileIcon;
    EditText etSearchUser;

    // Customer List Adapter
    RecyclerView customerListRecylcer;
    RecyclerView.LayoutManager customerListLayoutManager;
    CustomersRowAdapter customerListAdapter;
    /////////////////////////////////

    ArrayAdapter<String> usersList;

    //    ArrayList<UserSchema> userSchemaList = new ArrayList<>();
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        globalCode = GlobalCode.getInstance(MainActivity.this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Setting navigation bar
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        bindVariables();
        bindEvents();

        // adding login in details in navigation panel
        tvUserName.setText(globalCode.getPersonName());
        tvUserEmail.setText(globalCode.getPersonEmail());
        Glide.with(this)
                .load(globalCode.getPersonPhoto())
                .into(ivProfileIcon);

        fetchCustomersList();

    }


    public void bindVariables() {

        tvMadeBy = findViewById(R.id.tv_madeby);
        View sideNav = navigationView.getHeaderView(0);
        tvUserName = sideNav.findViewById(R.id.tv_username);
        tvUserEmail = sideNav.findViewById(R.id.tv_user_mail);
        ivProfileIcon = sideNav.findViewById(R.id.iv_profile_icon);

        etSearchUser = findViewById(R.id.et_search_user);

        fmTransferAmount = findViewById(R.id.fm_transfer_amount);

        // binding frame layouts
        globalCode.setFmCustomerList(findViewById(R.id.fm_customer_list));
        globalCode.setFmCustomerDetails(findViewById(R.id.fm_customer_details));
        globalCode.setIvBack(findViewById(R.id.iv_back));
        globalCode.setTvNoTransactionFound(findViewById(R.id.tv_no_transaction_found));

        // Setting up adapters
        globalCode.setTransactionsListRecylcer(findViewById(R.id.rv_transactions_list));
        globalCode.getTransactionsListRecylcer().setHasFixedSize(true);
        globalCode.setTransactionsListLayoutManager(new LinearLayoutManager(this));

        customerListRecylcer = findViewById(R.id.rv_customers_list);
        customerListRecylcer.setHasFixedSize(true);
        customerListLayoutManager = new LinearLayoutManager(this);

        globalCode.setTvCustomerName(findViewById(R.id.tv_selected_user_name));
        globalCode.setTvCurrentBalance(findViewById(R.id.tv_current_balance));
        globalCode.setTvCustomerID(findViewById(R.id.tv_customer_id));
        globalCode.setTvEmail(findViewById(R.id.tv_email));
        globalCode.setTvTodaysDate(findViewById(R.id.tv_todaysdate));

    }


    public void bindEvents() {


        tvMadeBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.linkedin.com/in/akshaychavan7/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });


        globalCode.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCustomersList();
                globalCode.getFmCustomerList().setVisibility(View.VISIBLE);
                globalCode.getFmCustomerDetails().setVisibility(View.GONE);
                globalCode.getIvBack().setVisibility(View.GONE);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nv_logout:
                        logout();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                }

                return true;
            }
        });

        fmTransferAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View view = li.inflate(R.layout.transfer_amount_intent_layout, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.CustomDialog);
                alertDialogBuilder.setView(view);

                globalCode.setEtBeneficiaryName(view.findViewById(R.id.et_beneficiary_name));
                globalCode.setCustomersDropDownList();

                AlertDialog transferPopup = alertDialogBuilder.create();
                transferPopup.show();


                EditText etTransferAmount = view.findViewById(R.id.et_amount);
                Button close = view.findViewById(R.id.cancel);
                Button transfer = view.findViewById(R.id.transfer);

                transfer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(globalCode.getEtBeneficiaryName().length()==0) {
                            Toast.makeText(MainActivity.this, "Beneficiary details field cannot be blank!\nPlease input beneficiary details correctly.", Toast.LENGTH_SHORT).show();
                        } else if(etTransferAmount.getText().length()==0) {
                            Toast.makeText(MainActivity.this, "Amount field cannot be blank!", Toast.LENGTH_SHORT).show();
                        } else {
                            String bkID = globalCode.getEtBeneficiaryName().getText().toString().split("BK")[1];       // Akshay Chavan (BK0001)
                            for (int i = 0; i < bkID.length(); i++) {
                                if (bkID.charAt(i) != '0') {
                                    bkID = bkID.substring(i, bkID.length()-1);
                                    break;
                                }
                            }

                            //updating current balance on textview
                            String currBal = globalCode.getTvCurrentBalance().getText().toString();
                            currBal = currBal.substring(1, currBal.length()-3);
                            int currentBalance = Integer.parseInt(currBal.replace(",",""))-Integer.parseInt(etTransferAmount.getText().toString());
                            globalCode.getTvCurrentBalance().setText("$"+String.format("%,d", currentBalance)+".00");
                            ////////////////////

                            dbAccess.openConnection();
                            boolean isTranferSuccessful = dbAccess.transferAmount(globalCode.selectedCustomerID, Integer.parseInt(bkID), Integer.parseInt(etTransferAmount.getText().toString()));
                            dbAccess.closeConnection();

                            if(isTranferSuccessful) {
                                globalCode.fetchTransactionsForUser();
                                transferPopup.dismiss();
                                Toast.makeText(MainActivity.this, "Amount Transferred Successfully!", Toast.LENGTH_SHORT).show();
                            }

                        }



                    }
                });


                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transferPopup.dismiss();
                    }
                });
            }
        });

        etSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customerListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void fetchCustomersList() {
        dbAccess = DBAccess.getInstance(getApplicationContext());
        dbAccess.openConnection();
//        dbAccess.transferAmount(1,2,1000);

        // fetching all users' data from database and preparing arraylist
        Cursor cursor = dbAccess.fetchAllUsersData();
        globalCode.getUserSchemaList().clear();
        while (cursor.moveToNext()) {
            globalCode.getUserSchemaList().add(new UserSchema(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3)));
        }
        dbAccess.closeConnection();


        // Passing data to Adapter
        customerListAdapter = new CustomersRowAdapter(globalCode.getUserSchemaList(), MainActivity.this);
        customerListRecylcer.setLayoutManager(customerListLayoutManager);
        customerListRecylcer.setAdapter(customerListAdapter);
    }

    private void logout() {
        if(globalCode.getLoggedInWith().equalsIgnoreCase("Facebook")) {
            Log.e(TAG, "Facebook logout");
            // Facebook logout
            LoginManager.getInstance().logOut();
            Toast.makeText(MainActivity.this, "You have successfully logged out!", Toast.LENGTH_SHORT).show();
            //////////////////
        }
        else if(globalCode.getLoggedInWith().equalsIgnoreCase("Google")) {
            // Google logout

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean(LOGIN_STATE, false);
            editor.apply();

            GoogleSignInClient mGoogleSignInClient = globalCode.getGoogleSignInClient();

            mGoogleSignInClient.signOut().addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, "You have successfully logged out!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }



    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }
}