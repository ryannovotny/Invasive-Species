package com.florafinder.invasive_species;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 * TODO: Implement our  own server for authentication
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private EditText mPasswordView, mNewPasswordView, mConfirmPasswordView,
            mNameView, mNewEmailView, mEmailView;
    private View mProgressView;
    private View mLoginFormView;

    private final static String USER_DIRECTORY = "/userData";
    private final static String SERVER_IP = "https://lempo.d.umn.edu";
    private final static String SERVER_PORT = ":4097";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

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
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });


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
     * Attempts to sign in
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

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
        Intent startMapIntent = new Intent(LoginActivity.this, MainActivity.class);

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
            JSONArray jsonArray = (JSONArray) jsonObject.get("results");

            //Email is not registered
            if(!email.equals(jsonArray.getJSONObject(0).get("email"))) {
                mEmailView.setError(getString(R.string.error_invalid_email));
            }
            //Password is incorrect
            else if(!password.equals(jsonArray.getJSONObject(0).get("password"))) {
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
            task.cancel(true);
        }
        catch(JSONException err){
            Log.e("LOGIN PUT", "Error parsing JSON result");
        }
    }

    /**
     * Attempts to sign up
     * If errors occur or the email already exists, no signup is made
     */
    private void attemptSignup(){
        // Reset errors.
        mNewEmailView.setError(null);
        mNewPasswordView.setError(null);
        mConfirmPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String email = mNewEmailView.getText().toString();
        String password = mNewPasswordView.getText().toString();
        String confirmPassword = mConfirmPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        //confirm name exists
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mNewPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mNewPasswordView;
            cancel = true;
        }

        // Check for matching password confirmation
        if (!TextUtils.isEmpty(confirmPassword) && !password.equals(confirmPassword)) {
            mConfirmPasswordView.setError(getString(R.string.error_incorrect_confirm_password));
            focusView = mConfirmPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mNewEmailView.setError(getString(R.string.error_field_required));
            focusView = mNewEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mNewEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mNewEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            attemptSignUpConnect(name, password, email);
        }
    }

    /**
     * Attempts to make POST connection to server
     * On success creates new account
     * @param name
     * @param password
     * @param email
     */
    private void attemptSignUpConnect(String name, String password, String email){

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        RestAsyncTask task = new RestAsyncTask();

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name);
            jsonObject.put("email", email);
            jsonObject.put("password", password);

            task.execute(SERVER_IP + SERVER_PORT + USER_DIRECTORY,"POST",jsonObject.toString());

            //attempt to get results
            String results = task.get();

            jsonObject = new JSONObject(results);
            if((Boolean) jsonObject.get("exists")) {
                mNewEmailView.setError(getString(R.string.error_invalid_email));
            }
            else {
                startActivity(intent);
            }
        }

        //Error handling
        catch(InterruptedException err){
            Log.e("SignUp POST", "Execution interrupted");
        }
        catch(ExecutionException err){
            Log.e("SignUp POST", "Execution failed");
            task.cancel(true);
        }
        catch(JSONException err){
            Log.e("SignUp POST", "Error parsing JSON object");
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

