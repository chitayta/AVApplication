package com.tma.tctay.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.tma.tctay.android.UserSessionManager;
import com.tma.tctay.utility.NetworkUtility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A login screen that offers login via email/password.
 */
public class AirVantageLoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    //private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
//    private static final String[] DUMMY_CREDENTIALS = new String[]{
//            "foo@example.com:hello", "bar@example.com:world"
//    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText mSystemUidView;
    private EditText mClientIdView;
    private EditText mClientSecretView;
    private View mProgressView;
    private View mLoginFormView;

    private UserSessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_vantage_login);
        session = new UserSessionManager(getApplicationContext());

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.emailInput);
        //populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.passwordInput);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mSystemUidView = (EditText) findViewById(R.id.systemUid);
        mClientIdView = (EditText) findViewById(R.id.clientid);
        mClientSecretView = (EditText) findViewById(R.id.clientsecret);
        Button mEmailSignInButton = (Button) findViewById(R.id.signInButton);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

//    private void populateAutoComplete() {
//        if (!mayRequestContacts()) {
//            return;
//        }
//
//        getLoaderManager().initLoader(0, null, this);
//    }
//
//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }
//
//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_READ_CONTACTS) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                populateAutoComplete();
//            }
//        }
//    }


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
        mSystemUidView.setError(null);
        mClientIdView.setError(null);
        mClientSecretView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String systemUid = mSystemUidView.getText().toString();
        String clientId = mClientIdView.getText().toString();
        String clientSecret = mClientSecretView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("This field is required");
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
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

        if (!isInputValid(systemUid)) {
            mSystemUidView.setError(getString(R.string.error_field_required));
            focusView = mSystemUidView;
            cancel = true;
        }

        if (!isInputValid(clientId)) {
            mClientIdView.setError(getString(R.string.error_field_required));
            focusView = mClientIdView;
            cancel = true;
        }

        if (!isInputValid(clientSecret)) {
            mClientSecretView.setError(getString(R.string.error_field_required));
            focusView = mClientSecretView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            if (!NetworkUtility.isNetworkAvailable(getApplicationContext()))
            {
                Toast.makeText(getApplicationContext(), "No network connection!",Toast.LENGTH_LONG).show();
            } else{
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                showProgress(true);
                mAuthTask = new UserLoginTask(email, password, systemUid, clientId, clientSecret);
                mAuthTask.execute((Void) null);
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isInputValid(String input) {
        return input.length() > 0;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 0;
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

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(AirVantageLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        //int IS_PRIMARY = 1;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, JSONObject> {

        private final String mEmail;
        private final String mPassword;
        private final String mSystemUid;
        private final String mClientId;
        private final String mClientSecret;
        private JSONObject return_response;

        public UserLoginTask(String email, String password, String systemUid, String clientId, String clientSecret) {
            mEmail = email;
            mPassword = password;
            mSystemUid = systemUid;
            mClientId = clientId;
            mClientSecret = clientSecret;
            return_response = null;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                //Simulate network access.
                Thread.sleep(2000);
                String url = "https://eu.airvantage.net/api/oauth/token?grant_type=password&username=" + mEmail + "&password=" + mPassword + "&client_id="+mClientId+"&client_secret="+mClientSecret;

                RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

                RequestFuture<JSONObject> future = RequestFuture.newFuture();

                JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(), future, future);


                mRequestQueue.add(request);

                try {
                    System.out.println("Getting response from request...");
                    JSONObject response = future.get(); // this will block

                    System.out.println("Response from request: " + response);
                    return_response = response;
                } catch (InterruptedException e) {
                    // exception handling
                    System.out.println("InterruptedException: " + e.getMessage());
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // exception handling
                    System.out.println("ExecutionException: " + e.getMessage());
                    e.printStackTrace();
                }
                System.out.println("Successful to get response from request.");
            } catch (InterruptedException e) {
                System.out.println("InteruptException: " + e.getMessage());
                e.printStackTrace();
            }

            return return_response;
        }



        //protected void onPostExecute(final Boolean success)
        @Override
        protected void onPostExecute(final JSONObject response) {
            mAuthTask = null;
            showProgress(false);
            if (response == null)
            {
                return;
            }
            if (response.has("error") && response.has("error_description")) {
//                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
//                alert.setTitle("Bad credential!");
//                // alert.setMessage("Message");
//
//                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //Your action here
//                    }
//                });
            } else {
                if (response.has("access_token") && response.has("token_type") && response.has("refresh_token") && response.has("expires_in"))
                {

                    try {
                        String access_token =  response.getString("access_token");
                        String token_type = response.getString("token_type");
                        String refresh_token = response.getString("refresh_token");
                        Integer expires_in = response.getInt("expires_in");

                        Intent intent = new Intent(getApplicationContext(), MainTabDataViewActivity.class);

                        intent.putExtra("systemUid", mSystemUid);
                        intent.putExtra("access_token", access_token);
                        intent.putExtra("token_type", token_type);
                        intent.putExtra("refresh_token", refresh_token);
                        intent.putExtra("expires_in", expires_in);

                        session.createUserLoginSession(mEmail, mSystemUid, mClientId, mClientSecret, access_token, refresh_token, token_type, expires_in);
                        startActivity(intent);
                        overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
                        finish();

                    }
                    catch (Exception exeption)
                    {
                        System.out.println("EXCETION HERE:" + exeption.getMessage());
                    }
                } else {
                    return_response = null;
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */


