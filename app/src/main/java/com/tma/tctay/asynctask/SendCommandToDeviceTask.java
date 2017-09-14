package com.tma.tctay.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.tma.tctay.models.AccessToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by tctay on 6/28/2017.
 */

public class SendCommandToDeviceTask extends AsyncTask<Void, Boolean, String>{

    private String systemUid;
    private AccessToken accessTokenResponse;
    private String return_response;
    private Context appContext;
    private boolean isTurnOnLed;
    private ProgressDialog progressDialog;
    //private ProgressBar dataWaitingProgressBar;

    public SendCommandToDeviceTask(String systemUid, AccessToken accessTokenResponse, boolean isTurnOnLed, Context appContext) {
        this.accessTokenResponse = accessTokenResponse;
        System.out.println("Access Token: " + this.accessTokenResponse.getAccess_token());
        this.return_response = null;
        this.appContext = appContext;
        this.isTurnOnLed = isTurnOnLed;
        this.systemUid = systemUid;
    }

    @Override
    protected void onPreExecute()
    {
        progressDialog = ProgressDialog.show(appContext, "Processing", "Led is turning "+ (isTurnOnLed?"on":"off") + "...",true);
    }

    @Override
    protected String doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.
        try {
            String access_token = accessTokenResponse.getAccess_token();
            String token_type = accessTokenResponse.getToken_type();
            String refesh_token = accessTokenResponse.getRefresh_token();
            Integer expires_in = accessTokenResponse.getExpires_in();
            AccessToken accessToken = new AccessToken(access_token, token_type, refesh_token, expires_in);

            //String dataUrl = "https://eu.airvantage.net/api/v1/systems/data/raw?targetIds=a26f0e4d012b40b0a4415492eff303bb&dataIds=testcenter-iot-system.temperature,testcenter-iot-system.motion&access_token="+accessToken.getAccess_token();
            String dataUrl = "https://eu.airvantage.net/api/v1/operations/systems/command?access_token=" + accessToken.getAccess_token();
            try {
                JSONObject bodyRequest = new JSONObject("{\n" +
                        "    \"systems\" : {\n" +
                        "       \"uids\" : [\""+systemUid+"\"]\n" +
                        "    },\n" +
                        "    \"commandId\" : \"tht.setAction\",\n" +
                        "    \"parameters\" :\n" +
                        "       {\n" +
                        "          \"action\" : " + (isTurnOnLed?"\"true\"":"\"false\"") +"\n" +
                        "       }\n" +
                        "}\n");


                RequestQueue mRequestQueue = Volley.newRequestQueue(appContext.getApplicationContext());
                RequestFuture<JSONObject> future = RequestFuture.newFuture();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST ,dataUrl, bodyRequest, future, future);
                mRequestQueue.add(request);

                JSONObject response = future.get(); // this will block

                System.out.println("Sending command RESPONSE: " + response.toString());

                return_response = response.getString("operation");

                return return_response;

            } catch (JSONException e) {
                //textView.setText("Error when parse json:\n" + e.getMessage());
                System.out.println("JSON Exception Error:" + e.getMessage());
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println("Execution Exception Error: " + e.getMessage());
                e.printStackTrace();
            };

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return return_response;
    }

    //    @Override
//    protected void onProgressUpdate(Boolean... value)
//    {
//        if (value[0])
//        {
//            //showProgress(true);
//        }
//        else
//        {
//            //showProgress(false);
//        }
//    }
    @Override
    protected void onPostExecute(final String operationId) {
        progressDialog.dismiss();
        System.out.println("DONE: " + operationId);
        Toast.makeText(appContext, "Successfully sent command to turn led " + (isTurnOnLed?"on!":"off!"),Toast.LENGTH_LONG).show();
    }
}
