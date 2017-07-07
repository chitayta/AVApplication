package com.tma.tctay.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.tma.tctay.android.UserSessionManager;
import com.tma.tctay.models.AccessToken;
import com.tma.tctay.utility.NetworkUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ImageButton goButton;
    private UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());
        goButton = (ImageButton) findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Intent intent = new Intent(getApplicationContext(), AirVantageLoginActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
                if (NetworkUtility.isNetworkAvailable(getApplicationContext()))
                {
                    if (session.checkLogin()) {

                        System.out.println("MainActivity: ---> AirVantageLoginActivity");
                        Intent intent = new Intent(getApplicationContext(), AirVantageLoginActivity.class);

                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                        overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
                        finish();

                    }
                    else
                    {
                        System.out.println("MainActivity: ---> MainTabDataViewActivity");

                        HashMap<String, String> userCredentials = session.getUserCredentials();
                        AccessToken accessToken = session.getAccessToken();

                        String access_token = accessToken.getAccess_token();
                        String token_type = accessToken.getToken_type();
                        String refresh_token = accessToken.getRefresh_token();
                        Integer expires_in = accessToken.getExpires_in();

                        (new RefreshTokenTask(refresh_token, userCredentials.get("systemUid"), userCredentials.get("clientId"), userCredentials.get("clientSecret"))).execute();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No network connection!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    //https://eu.airvantage.net/api/oauth/token?grant_type=refresh_token&refresh_token=3d8e4881-bddc-4c64-8bd0-758a70243469&client_id=fd9304505edb4334896f784a4b315974&client_secret=d2b1f104b80b4dbdb8b763cd01fdab93

    private class RefreshTokenTask extends AsyncTask<Void, Void, JSONObject> {

        private final String refresh_token;
        private final String systemUid;
        private final String clientId;
        private final String clientSecret;

        public RefreshTokenTask(String refresh_token, String systemUid, String clientId, String clientSecret) {
            this.refresh_token = refresh_token;
            this.systemUid = systemUid;
            this.clientId = clientId;
            this.clientSecret = clientSecret;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                String url = "https://eu.airvantage.net/api/oauth/token?grant_type=refresh_token&refresh_token="+refresh_token+"&client_id="+clientId+"&client_secret="+clientSecret;

                RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());

                RequestFuture<JSONObject> future = RequestFuture.newFuture();

                JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(), future, future);

                mRequestQueue.add(request);

                System.out.println("Getting response from request...");
                JSONObject response = future.get(); // this will block
                System.out.println("Response from request: " + response);

                System.out.println("Successful to get response from request.");

                return response;
            } catch (InterruptedException e) {
                // exception handling
                System.out.println("InterruptedException: " + e.getMessage());
                e.printStackTrace();
            } catch (ExecutionException e) {
                // exception handling
                System.out.println("ExecutionException: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }



        //protected void onPostExecute(final Boolean success)
        @Override
        protected void onPostExecute(final JSONObject response) {
            if (response == null)
            {
                Toast.makeText(getApplicationContext(), "Something wrong...",Toast.LENGTH_LONG).show();
                return;
            }
            if (response.has("error") && response.has("error_description")) {

                Toast.makeText(getApplicationContext(), "Something wrong...",Toast.LENGTH_LONG).show();
                finish();

            } else {
                if (response.has("access_token") && response.has("token_type") && response.has("refresh_token") && response.has("expires_in"))
                {

                    try {
                        Intent intent = new Intent(getApplicationContext(), MainTabDataViewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        String access_token =  response.getString("access_token");
                        String token_type = response.getString("token_type");
                        String refresh_token = response.getString("refresh_token");
                        Integer expires_in = response.getInt("expires_in");

                        session.refreshAccessToken(access_token, refresh_token, token_type, expires_in);

                        intent.putExtra("systemUid", this.systemUid);
                        System.out.println("System UID: " + this.systemUid);
                        intent.putExtra("access_token", access_token);
                        intent.putExtra("token_type", token_type);
                        intent.putExtra("refresh_token", refresh_token);
                        intent.putExtra("expires_in", expires_in);

                        startActivity(intent);
                        overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something wrong...",Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Something wrong...",Toast.LENGTH_LONG).show();
                }
            }
        }

    }
//    /** Called when the user taps the Send button */
//    public void getViewDataActivity(View view) {
//        // Do something in response to button
//
////        Intent intent = new Intent(this, AirVantageLoginActivity.class);
////        startActivity(intent);
////        overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
//        //EditText updateEditText = (EditText)findViewById(R.id.updateTextView);
//        //updateEditText.setText("Pressed!");
//
//    }
//    public class GetAccessTokenTask extends AsyncTask<Void, Void, JSONObject> {
//
//        private final String mEmail;
//        private final String mPassword;
//        private final String mClientId;
//        private final String mClientSecret;
//        private JSONObject return_response;
//        private Context appContext;
//
//        public GetAccessTokenTask(String email, String password, String clientId, String clientSecret, Context appContext) {
//            mEmail = email;
//            mPassword = password;
//            mClientId = clientId;
//            mClientSecret = clientSecret;
//            return_response = null;
//            this.appContext = appContext;
//        }
//
//        @Override
//        protected JSONObject doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//                //Simulate network access.
//                Thread.sleep(2000);
//                String url = "https://eu.airvantage.net/api/oauth/token?grant_type=password&username=" + mEmail + "&password=" + mPassword + "&client_id="+mClientId+"&client_secret="+mClientSecret;
//
//                RequestQueue mRequestQueue = Volley.newRequestQueue(appContext);
//
//                RequestFuture<JSONObject> future = RequestFuture.newFuture();
//
//                JsonObjectRequest request = new JsonObjectRequest(url, new JSONObject(), future, future);
//
//
//                mRequestQueue.add(request);
//
//                try {
//                    System.out.println("TCT");
//                    JSONObject response = future.get(); // this will block
//                    //response.get
//                    //System.out.println("responssssee: " + response);
//                    return_response = response;
//                } catch (InterruptedException e) {
//                    // exception handling
//                    System.out.println("HERE"  + e.getMessage());
//                } catch (ExecutionException e) {
//                    // exception handling
//                    System.out.println("HEE " + e.getMessage());
//                }
//                System.out.println("CCT");
//                // TODO: register the new account here
//                //Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return return_response;
//        }
//
//
//
//        //protected void onPostExecute(final Boolean success)
//        @Override
//        protected void onPostExecute(final JSONObject response) {
//            if (response == null)
//            {
//                return;
//            }
//            if (response.has("error") && response.has("error_description")) {
//    //                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
//    //                alert.setTitle("Bad credential!");
//    //                // alert.setMessage("Message");
//    //
//    //                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//    //                    public void onClick(DialogInterface dialog, int whichButton) {
//    //                        //Your action here
//    //                    }
//    //                });
//            } else {
//                if (response.has("access_token") && response.has("token_type") && response.has("refresh_token") && response.has("expires_in"))
//                {
//                    try {
//                        String access_token =  response.getString("access_token");
//                        String token_type = response.getString("token_type");
//                        String refresh_token = response.getString("refresh_token");
//                        Integer expires_in = response.getInt("expires_in");
//
//                        Intent intent = new Intent(appContext, MainTabDataViewActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        intent.putExtra("access_token", access_token);
//                        intent.putExtra("token_type", token_type);
//                        intent.putExtra("refresh_token", refresh_token);
//                        intent.putExtra("expires_in", expires_in);
//
//                        appContext.startActivity(intent);
//                    }
//                    catch (Exception exeption)
//                    {
//                        System.out.println("EXCETION HERE:" + exeption.getMessage());
//                    }
//                } else {
//                    return_response = null;
//                }
//            }
//
//        }
//    }
}
