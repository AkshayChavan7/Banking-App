package com.akshaychavan.bankingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.akshaychavan.bankingapp.utility.GlobalCode;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "sharedprefs";
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";
    public static final String LOGIN_STATE = "login";
    public static final String USER_PHOTO = "photo";
    public static final String GOOGLE_SIGN_IN_ACC = "google_sign_in_account";
    private static final int RC_SIGN_IN = 0;
    private static final String EMAIL = "email";
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
    ConstraintLayout clLoginMain;
    GlobalCode globalCode;
    LoginButton loginButton;
    ImageView ivGoogleLogin, ivFacebookLogin;
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                LoginManager.getInstance().logOut();
            }
        }
    };
    private AnimationDrawable animationDrawable;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        globalCode = GlobalCode.getInstance(getApplicationContext());


        //Facebook login
        facebookLogin();

        ////////


        ////////////////////////////////////////////////////////////////////////
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        checkLoginState();      // check login state, if already logged in then directly open main activity
        bindVariables();
        bindEvents();
        setBackgroundAnimation();
        loadLoginDetails();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);


//        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
//                new GraphRequest.GraphJSONObjectCallback() {
//
//                    @Override
//                    public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
//                        Log.e(TAG, jsonObject.toString());
//                        try {
//                            globalCode.setPersonName(jsonObject.getString("name"));
//                            globalCode.setPersonEmail(jsonObject.getString("email"));
//                            Uri photoURI = Uri.parse("https://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=large");
//                            globalCode.setPersonPhoto(photoURI);
//
//                            Log.e(TAG, jsonObject.getString("name")+" "+jsonObject.getString("email")+" "+photoURI);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }
//        );
//
//        Bundle bundle = new Bundle();
//        bundle.putString("fields", "gender, name, id, first_name, last_name, email");
//
//        graphRequest.setParameters(bundle);
//        graphRequest.executeAsync();


//        Log.e(TAG, "onActivityResult>>" + requestCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }

    public void bindVariables() {
        ivGoogleLogin = findViewById(R.id.iv_google_login);
        ivFacebookLogin = findViewById(R.id.iv_google_login);
        clLoginMain = findViewById(R.id.cl_login_main);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        signinButton = findViewById(R.id.signin);
        titleText = findViewById(R.id.tv_title);
    }

    public void bindEvents() {

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalCode.setPersonName("Akshay Chavan");
                globalCode.setPersonEmail("akshay.chavan@gmail.com");
                globalCode.setPersonPhoto(Uri.parse("https://lh3.googleusercontent.com/a-/AOh14Gj06W0Nv4pMz5pbuqXDkxJatTgcBHJKAFzSe9PZ8uA"));
                proceedNextActivity();
            }
        });

        ivFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Facebook login clicked!");
            }
        });

        ivGoogleLogin.setOnClickListener(new View.OnClickListener() {
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


    public void facebookLogin() {
        callbackManager = CallbackManager.Factory.create();


        loginButton = findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

//        if(isLoggedIn) {
////            proceedNextActivity();
//        } else {
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "Facebook login");
                // App code
                GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                                Log.e(TAG, jsonObject.toString());
                                try {

                                    personName = jsonObject.getString("name");
                                    personEmail = jsonObject.getString("email");
                                    personPhoto = Uri.parse("https://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=large");
                                    saveLoginDetails();

                                    globalCode.setPersonName(personName);
                                    globalCode.setPersonEmail(personEmail);
                                    globalCode.setPersonPhoto(personPhoto);
                                    globalCode.setLoggedInWith("Facebook");

                                    Log.e(TAG, jsonObject.getString("name") + " " + jsonObject.getString("email") + " " + personPhoto);
                                    proceedNextActivity();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                );

                Bundle bundle = new Bundle();
                bundle.putString("fields", "gender, name, id, first_name, last_name, email");

                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                Log.e(TAG, "Facebook login-onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e(TAG, "Facebook login-onError\n"+exception.getMessage());
            }
        });
    }
//    }


    private void setBackgroundAnimation() {
        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) clLoginMain.getBackground();

        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(10);

        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);

        animationDrawable.start();
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
            globalCode.setLoggedInWith("Google");
            loginState = true;      // login success
//            }

            globalCode.setAccountDetails(acct);


            // save login details before moving to next activity
            saveLoginDetails();

            proceedNextActivity();
        } else {
            Log.e(TAG, "Google Account Details is NULL");
        }
    }


    // Saving user login data with Shared Preferences
    public void saveLoginDetails() {
        editor.putString(USER_NAME, personName);
        editor.putString(USER_EMAIL, personEmail);
        editor.putString(USER_PHOTO, personPhoto.toString());
        editor.putBoolean(LOGIN_STATE, loginState);

        editor.apply();
    }

    // Fetching user's previous login details if previously logged in
    public void loadLoginDetails() {
        etName.setText(sharedPreferences.getString(USER_NAME, ""));
        etEmail.setText(sharedPreferences.getString(USER_EMAIL, ""));
        loginState = sharedPreferences.getBoolean(LOGIN_STATE, false);


    }


    public void proceedNextActivity() {
        LoginManager.getInstance().logOut();

        // move to next activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }


}