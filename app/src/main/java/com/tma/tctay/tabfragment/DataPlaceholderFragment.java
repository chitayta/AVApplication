package com.tma.tctay.tabfragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.tma.tctay.activity.R;
import com.tma.tctay.asynctask.GetDataFromCloudTask;
import com.tma.tctay.models.AccessToken;
import com.tma.tctay.models.CloudApplicationModel;
import com.tma.tctay.models.Motion;
import com.tma.tctay.models.ReceivedLastDataPoint;
import com.tma.tctay.models.Temperature;
import com.tma.tctay.utility.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by tctay on 6/23/2017.
 */


/**
 * A placeholder fragment containing a simple view.
 */

public class DataPlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private View rootView;
    private String systemUid;
    private AccessToken accessToken;

    private SwipeRefreshLayout swipeRefreshLayoutData;

    public DataPlaceholderFragment() {

    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public void setSystemUid(String systemUid) {
        this.systemUid = systemUid;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DataPlaceholderFragment newInstance(int sectionNumber, AccessToken accessToken, String systemUid) {
        DataPlaceholderFragment fragment = new DataPlaceholderFragment();
        fragment.setAccessToken(accessToken);
        fragment.setSystemUid(systemUid);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        switch (getArguments().getInt("section_number"))
//        {
//
//            case 1:
//                rootView = inflater.inflate(R.layout.fragment_system_main_tab_data_view, container, false);
//                break;
//
//            case 3:
//                rootView = inflater.inflate(R.layout.fragment_command_main_tab_data_view, container, false);
//                break;
//            case 4:
//                rootView = inflater.inflate(R.layout.fragment_dashboard_main_tab_data_view, container, false);
//                break;
//            case 2:
                new GetDataFromCloudTask(systemUid, accessToken, getContext(), getActivity()).execute();

                rootView = inflater.inflate(R.layout.fragment_data_main_tab_data_view, container, false);
                swipeRefreshLayoutData = rootView.findViewById(R.id.swiperefreshdata);
                swipeRefreshLayoutData.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
//                swipeRefreshLayoutData.setColorScheme(
//                        getResources().getColor(R.color.swipe_color_1), getResources().getColor(R.color.swipe_color_2),
//                        getResources().getColor(R.color.swipe_color_3), getResources().getColor(R.color.swipe_color_4));
                //((Button) rootView.findViewById(timestampTemperatureDataViewButton)).setText("Ta Chi Tay");
                //System.out.println("ROOT1" + rootView.toString());
//                break;
//        }
        //System.out.println("ABCXYZ: " + );

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments().getInt("section_number") == 2) {
            swipeRefreshLayoutData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //Log.i(LOG_TAG, "onRefresh called from SwipeRefreshLayout");
                    System.out.println("onRefresh called from SwipeRefreshLayout");
                    initiateRefresh();
                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle state)
    {
        super.onActivityCreated(state);

    }

    public View getRootView()
    {
        return this.rootView;
    }

    private void initiateRefresh()
    {
        System.out.println("initiateRefresh");
        if (NetworkUtility.isNetworkAvailable(getContext()))
        {
            (new RefreshDataTask(this.systemUid, this.accessToken, getContext(), getActivity())).execute();
        }
        else
        {
            swipeRefreshLayoutData.setRefreshing(false);
            Toast.makeText(getContext(), "No network connection!",Toast.LENGTH_LONG).show();
        }
    }

    public class RefreshDataTask extends AsyncTask<Void, Boolean, ReceivedLastDataPoint> {

        private String systemUid;
        private AccessToken accessTokenResponse;
        private ReceivedLastDataPoint return_response;
        private Context appContext;
        private Activity appActivity;
        //private ProgressDialog progressDialog;
        //private ProgressBar dataWaitingProgressBar;
        public RefreshDataTask(String systemUid, AccessToken accessTokenResponse, Context appContext, Activity appActivity) {

            this.systemUid = systemUid;
            this.accessTokenResponse = accessTokenResponse;
            this.return_response = null;
            this.appContext = appContext;
            this.appActivity = appActivity;

        }
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
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
                    System.out.println("RESPONSE: " + response.toString());
                    JSONArray temperatureDataJsonArray = response.getJSONArray(CloudApplicationModel.temperatureId);
                    JSONArray motionDataJsonArray = response.getJSONArray(CloudApplicationModel.motionId);
                    if (temperatureDataJsonArray.length() == 1 && motionDataJsonArray.length() == 1) {
                        Temperature temperature = new Temperature(Float.parseFloat(temperatureDataJsonArray.getJSONObject(0).getString("value")), temperatureDataJsonArray.getJSONObject(0).getLong("timestamp"));
                        Motion motion = new Motion(motionDataJsonArray.getJSONObject(0).getString("value"), motionDataJsonArray.getJSONObject(0).getLong("timestamp"));
                        ReceivedLastDataPoint receivedLastDataPoint = new ReceivedLastDataPoint(temperature, motion);
                        System.out.println("DATAA:" + receivedLastDataPoint.getTemperatureData().getTemperatureData().toString());
                        return receivedLastDataPoint;
                    } else {
                        return return_response;
                    }
                } catch (JSONException e) {

                    System.out.println("JSONException: " + e.getMessage());
                    e.printStackTrace();

                } catch (ExecutionException e) {

                    System.out.println("ExecutionException: " + e.getMessage());
                    e.printStackTrace();

                }
            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
                e.printStackTrace();

            }
            return return_response;
        }

        @Override
        protected void onPostExecute(final ReceivedLastDataPoint receivedLastDataPoint) {
            if (receivedLastDataPoint == null)
            {
                Toast.makeText(appContext, "Something wrong...", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                Button timestampTemperatureDataViewButton = (Button) appActivity.findViewById(R.id.timestampTemperatureDataViewButton);
                timestampTemperatureDataViewButton.setText(receivedLastDataPoint.getTemperatureData().getTemperatureTimeStampString());

                Button temperatureDataViewButton = (Button) appActivity.findViewById(R.id.temperatureDataViewButton);
                temperatureDataViewButton.setText(receivedLastDataPoint.getTemperatureData().getTemperatureData().toString());//+ "      " + (char) 0x00B0 + "C");

                Button timestampMotionDataViewButton = (Button) appActivity.findViewById(R.id.timestampMotionDataViewButton);
                timestampMotionDataViewButton.setText(receivedLastDataPoint.getMotionData().getMotionTimeStampString());

                Button motionDataViewButton = (Button) appActivity.findViewById(R.id.motionDataViewButton);
                motionDataViewButton.setText(receivedLastDataPoint.getMotionData().getMotionData());

                swipeRefreshLayoutData.setRefreshing(false);

                Toast.makeText(appContext, "Successfully refreshed data!", Toast.LENGTH_LONG).show();
            }
        }
    }
}

