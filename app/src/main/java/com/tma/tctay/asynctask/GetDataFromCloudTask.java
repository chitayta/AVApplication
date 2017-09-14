package com.tma.tctay.asynctask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.tma.tctay.activity.R;
import com.tma.tctay.models.AccessToken;
import com.tma.tctay.models.CloudApplicationModel;
import com.tma.tctay.models.Motion;
import com.tma.tctay.models.ReceivedLastDataPoint;
import com.tma.tctay.models.Temperature;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by tctay on 6/23/2017.
 */

public class GetDataFromCloudTask extends AsyncTask<Void, Boolean, ReceivedLastDataPoint> {

    private String systemUid;
    private AccessToken accessTokenResponse;
    private ReceivedLastDataPoint return_response;
    private Context appContext;
    private Activity appActivity;
    private ProgressDialog progressDialog;
    private ProgressBar dataWaitingProgressBar;
    public GetDataFromCloudTask(String systemUid, AccessToken accessTokenResponse, Context appContext, Activity appActivity) {

        this.systemUid = systemUid;
        this.accessTokenResponse = accessTokenResponse;
        this.return_response = null;
        this.appContext = appContext;
        this.appActivity = appActivity;

    }
    @Override
    protected void onPreExecute()
    {
        //progressDialog = ProgressDialog.show(appContext, "Loading", "Data is loading...",true);
    }

    @Override
    protected ReceivedLastDataPoint doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.
        try {
            String access_token = accessTokenResponse.getAccess_token();
            String token_type = accessTokenResponse.getToken_type();
            String refesh_token = accessTokenResponse.getRefresh_token();
            Integer expires_in = accessTokenResponse.getExpires_in();
            AccessToken accessToken = new AccessToken(access_token, token_type, refesh_token, expires_in);

            //String dataUrl = "https://eu.airvantage.net/api/v1/systems/data/raw?targetIds=a26f0e4d012b40b0a4415492eff303bb&dataIds=testcenter-iot-system.temperature,testcenter-iot-system.motion&access_token="+accessToken.getAccess_token();
            //String dataUrl = "https://eu.airvantage.net/api/v1/systems/a26f0e4d012b40b0a4415492eff303bb/data?ids=testcenter-iot-system.temperature,testcenter-iot-system.motion&access_token=" + accessToken.getAccess_token();
            String dataUrl = "https://eu.airvantage.net/api/v1/systems/" + systemUid + "/data?ids=tht.temperature,tht.motion&access_token=" + accessToken.getAccess_token();

            RequestQueue mRequestQueue = Volley.newRequestQueue(appContext.getApplicationContext());

            RequestFuture<JSONObject> future = RequestFuture.newFuture();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET ,dataUrl, new JSONObject(), future, future);


            mRequestQueue.add(request);

            try {
                JSONObject response = future.get(); // this will block
                System.out.println("Response: " + response.toString());
                JSONArray temperatureDataJsonArray = response.getJSONArray(CloudApplicationModel.temperatureId);
                JSONArray motionDataJsonArray = response.getJSONArray(CloudApplicationModel.motionId);
                if (temperatureDataJsonArray.length() == 1 && motionDataJsonArray.length() == 1) {
                    Temperature temperature = new Temperature(Float.parseFloat(temperatureDataJsonArray.getJSONObject(0).getString("value")), temperatureDataJsonArray.getJSONObject(0).getLong("timestamp"));
                    Motion motion = new Motion(motionDataJsonArray.getJSONObject(0).getString("value"), motionDataJsonArray.getJSONObject(0).getLong("timestamp"));
                    ReceivedLastDataPoint receivedLastDataPoint = new ReceivedLastDataPoint(temperature, motion);
                    return receivedLastDataPoint;
                } else {
                    return return_response;
                }
            } catch (JSONException e) {
                //textView.setText("Error when parse json:\n" + e.getMessage());
                System.out.println("Request URL: " + dataUrl);
                System.out.println("JSONException: " + e.getMessage());
                e.printStackTrace();
            } catch (ExecutionException e) {
                System.out.println("ExecutionException: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return return_response;
    }

    @Override
    protected void onPostExecute(final ReceivedLastDataPoint receivedLastDataPoint) {

        Button timestampTemperatureDataViewButton = (Button) appActivity.findViewById(R.id.timestampTemperatureDataViewButton);
        timestampTemperatureDataViewButton.setText(receivedLastDataPoint.getTemperatureData().getTemperatureTimeStampString());

        Button temperatureDataViewButton = (Button) appActivity.findViewById(R.id.temperatureDataViewButton);
        temperatureDataViewButton.setText(receivedLastDataPoint.getTemperatureData().getTemperatureData().toString());//+ "      " + (char) 0x00B0 + "C");

        Button timestampMotionDataViewButton = (Button) appActivity.findViewById(R.id.timestampMotionDataViewButton);
        timestampMotionDataViewButton.setText(receivedLastDataPoint.getMotionData().getMotionTimeStampString());

        Button motionDataViewButton = (Button) appActivity.findViewById(R.id.motionDataViewButton);
        motionDataViewButton.setText(receivedLastDataPoint.getMotionData().getMotionData());
        //progressDialog.dismiss();

    }

}