package com.florafinder.invasive_species;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 * TODO: Implement our  own server for authentication
 * TODO: Rewrite activity to extend our own REST async task
 * TODO: Afterwards, make this activity the main activity
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private RestAsyncTask mAuthTask = null;

    // UI references.
    private EditText mPasswordView, mNewPasswordView, mConfirmPasswordView,
            mNameView, mNewEmailView, mEmailView;
    private View mProgressView;
    private View mLoginFormView;

    private final static String USER_DIRECTORY = "/userdata";
    private final static String SERVER_IP = "131.212.217.220";
    private final static String SERVER_PORT = ":4321";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        initViews();
    }

    //////////////////////////////////////////////////////////////////////////////////
    //                  Private Methods

    /**
     * Initializes views to private variables
     */
    private void initViews() {

        //Login Texts
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        //SignUp Texts
        mNameView = (EditText) findViewById(R.id.name);
        mNewEmailView = (EditText) findViewById(R.id.new_email);
        mNewPasswordView = (EditText) findViewById(R.id.new_password);
        mConfirmPasswordView = (EditText) findViewById(R.id.confirm_password);


        //Setup login button
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //Setup signup button
        Button mSignUpButton = (Button) findViewById(R.id.email_sign_up_button);


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            attemptLogInConnect(email, password);
        }
    }

    /**
     * Attempts REST connection to server
     */
    private void attemptLogInConnect(String email, String password){

        String result = null;
        RestAsyncTask task = new RestAsyncTask();
        JSONObject jsonObject = new JSONObject();
        Intent startMapIntent = new Intent(LoginActivity.this, DrawerActivity.class);

        try {
            jsonObject.put("email", email);
        }
        catch(JSONException err){
            Log.e("LOGIN PUT", "Error creating json object");
            err.printStackTrace();
        }

        //Attempt connection
        task.execute(SERVER_IP + SERVER_PORT + USER_DIRECTORY,"PUT",jsonObject.toString());

        try {
            result = task.get();
            jsonObject = new JSONObject(result);

            //Email is not registered
            if(!email.equals(jsonObject.get("email"))) {
                mEmailView.setError(getString(R.string.error_invalid_email));
            }
            //Password is incorrect
            else if(!password.equals(jsonObject.get("password"))) {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
            }
            //Connection successful, push intent
            else{
                startActivity(startMapIntent);
            }
        }

        //Error handling from connect
        catch(InterruptedException err){
            Log.e("LOGIN PUT", "Connection interrupted");
        }
        catch(ExecutionException err){
            Log.e("LOGIN PUT", "Execution failed");
        }
        catch(JSONException err){
            Log.e("LOGIN PUT", "Error parsing JSON result");
        }
    }

    /**
     * Checks to make sure email is valid
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Checks to make sure password is at least 4 characters
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

