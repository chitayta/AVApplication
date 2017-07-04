package com.tma.tctay.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tma.tctay.android.UserSessionManager;
import com.tma.tctay.models.AccessToken;
import com.tma.tctay.utility.NetworkUtility;

import java.util.HashMap;

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
                    } else {
                        System.out.println("MainActivity: ---> MainTabDataViewActivity");
                        Intent intent = new Intent(getApplicationContext(), MainTabDataViewActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                        HashMap<String, String> systemUid = session.getUserCredentials();
                        AccessToken accessToken = session.getAccessToken();

                        String access_token = accessToken.getAccess_token();
                        String token_type = accessToken.getToken_type();
                        String refresh_token = accessToken.getRefresh_token();
                        Integer expires_in = accessToken.getExpires_in();

                        intent.putExtra("systemUid", systemUid.get("systemUid"));
                        intent.putExtra("access_token", access_token);
                        intent.putExtra("token_type", token_type);
                        intent.putExtra("refresh_token", refresh_token);
                        intent.putExtra("expires_in", expires_in);

                        startActivity(intent);
                        overridePendingTransition(R.transition.slide_from_right, R.transition.slide_to_left);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No network connection!",Toast.LENGTH_LONG).show();
                }
            }
        });
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
