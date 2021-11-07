package com.akshaychavan.bankingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.akshaychavan.bankingapp.utility.GlobalCode;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


public class LoginActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedprefs";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String LOGIN_STATE = "login";
    public static final String GOOGLE_SIGN_IN_ACC = "google_sign_in_account";
    private static final int RC_SIGN_IN = 0;
    private final String TAG = "LoginActivity";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;
    String personName, personGivenName, personFamilyName, personEmail, personId;
    Uri personPhoto;
    boolean loginState = false;     // default login state to be false

    EditText etName, etEmail;
    MaterialButton signinButton;
    TextView titleText;

    GlobalCode globalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        globalCode = GlobalCode.getInstance(getApplicationContext());


        ////////////////////////////////////////////////////////////////////////
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkLoginState();      // check login state, if already logged in then directly open main activity
        bindVariables();
        bindEvents();
        loadLoginDetails();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        Log.e(TAG, "onActivityResult>>" + requestCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    public void bindVariables() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        signinButton = findViewById(R.id.signin);
        titleText = findViewById(R.id.tv_title);
    }

    public void bindEvents() {
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

//                getUsersCount();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();


                // Build a GoogleSignInClient with the options specified by gso.
                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
                globalCode.setGoogleSignInClient(mGoogleSignInClient);

                signIn();

            }
        });
    }


    private void checkLoginState() {

        // if already logged iclienttn then go to MainActivity
        if (sharedPreferences.getBoolean(LOGIN_STATE, false)) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();


            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
            globalCode.setGoogleSignInClient(mGoogleSignInClient);

            signIn();
        }
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
        //           GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        getUserAccountInfo();
    }


    public void getUserAccountInfo() {

        if (acct != null) {
//            if (personEmail == null || !personEmail.equalsIgnoreCase(acct.getEmail())) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personEmail = acct.getEmail();
            personId = acct.getId();
            personPhoto = acct.getPhotoUrl();

            globalCode.setPersonName(personName);
            globalCode.setPersonEmail(personEmail);
            globalCode.setPersonPhoto(personPhoto);
            loginState = true;      // login success
//            }

            globalCode.setAccountDetails(acct);


            // save login details before moving to next activity
            saveLoginDetails();

            // move to next activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Log.e(TAG, "Google Account Details is NULL");
        }
    }



    // Saving user login data with Shared Preferences
    public void saveLoginDetails() {
        editor.putString(USER_NAME, personName);
        editor.putString(USER_EMAIL, personEmail);
        editor.putBoolean(LOGIN_STATE, loginState);

        editor.apply();
    }

    // Fetching user's previous login details if previously logged in
    public void loadLoginDetails() {
        etName.setText(sharedPreferences.getString(USER_NAME, ""));
        etEmail.setText(sharedPreferences.getString(USER_EMAIL, ""));
        loginState = sharedPreferences.getBoolean(LOGIN_STATE, false);


    }


}