package com.tma.tctay.tabfragment;

/**
 * Created by tctay on 6/27/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.tma.tctay.activity.R;
import com.tma.tctay.android.SystemListViewAdapter;
import com.tma.tctay.models.AccessToken;
import com.tma.tctay.models.ListViewSystemItemModel;
import com.tma.tctay.models.ReceivedLastDataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class SystemPlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView mListView;
    private SystemListViewAdapter mListAdapter;
    private AccessToken accessToken;
    private String systemUid;
    ArrayList<ListViewSystemItemModel> modelArrayList;

    public SystemPlaceholderFragment() {
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
    public static SystemPlaceholderFragment newInstance(int sectionNumber, AccessToken accessToken, String systemUid) {
        SystemPlaceholderFragment fragment = new SystemPlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setAccessToken(accessToken);
        fragment.setSystemUid(systemUid);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_system_main_tab_data_view, container, false);
        mListView = (ListView) rootView.findViewById(R.id.system_listView);
        modelArrayList = new ArrayList<ListViewSystemItemModel>();

        (new GetSystemInfoTask(this.systemUid, this.accessToken, getContext(), getActivity())).execute();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedStateInstance)
    {
        super.onViewCreated(view, savedStateInstance);



    }

    private class GetSystemInfoTask extends AsyncTask<Void, Void, Boolean> {

        private String systemUid;
        private AccessToken accessTokenResponse;
        private ReceivedLastDataPoint return_response;
        private Context appContext;
        private Activity appActivity;
        //private ProgressDialog progressDialog;
        //private ProgressBar dataWaitingProgressBar;
        public GetSystemInfoTask(String systemUid, AccessToken accessTokenResponse, Context appContext, Activity appActivity) {

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
           // progressDialog = ProgressDialog.show(appContext, "Loading", "System tab data is loading...", true);
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String access_token = accessTokenResponse.getAccess_token();
            String token_type = accessTokenResponse.getToken_type();
            String refesh_token = accessTokenResponse.getRefresh_token();
            Integer expires_in = accessTokenResponse.getExpires_in();
            AccessToken accessToken = new AccessToken(access_token, token_type, refesh_token, expires_in);
            RequestQueue mRequestQueue = Volley.newRequestQueue(appContext.getApplicationContext());
            RequestFuture<JSONObject> future = RequestFuture.newFuture();


            String userDataUrl = "https://eu.airvantage.net/api/v1/users/current?access_token=" + accessToken.getAccess_token();
            String deviceDataUrl = "https://eu.airvantage.net/api/v1/users/current?access_token=" + accessToken.getAccess_token();

            JsonObjectRequest userDataRequest = new JsonObjectRequest(Request.Method.GET , userDataUrl, new JSONObject(), future, future);
            JsonObjectRequest deviceDataRequest = new JsonObjectRequest(Request.Method.GET , deviceDataUrl, new JSONObject(), future, future);

            mRequestQueue.add(userDataRequest);

            try {
                modelArrayList.add(new ListViewSystemItemModel("User Detail"));

                JSONObject response = future.get(); // this will block
                System.out.println("USER DATA RESPONSE: " + response.toString());
                String email = response.getString("email");
                String name = response.getString("name");

                modelArrayList.add(new ListViewSystemItemModel("Email", email));
                modelArrayList.add(new ListViewSystemItemModel("Name", name));

                modelArrayList.add(new ListViewSystemItemModel("Device Detail"));

                mRequestQueue.add(deviceDataRequest);

            } catch (JSONException e) {

                System.out.println("JSONException: " + e.getMessage());
                e.printStackTrace();

            } catch (ExecutionException e) {

                System.out.println("ExecutionException: " + e.getMessage());
                e.printStackTrace();

            } catch (InterruptedException e) {

                System.out.println("InterruptedException: " + e.getMessage());
                e.printStackTrace();

            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean signal) {

            mListAdapter = new SystemListViewAdapter(getContext(), modelArrayList, accessToken);
            mListView.setAdapter(mListAdapter);
            //progressDialog.dismiss();
        }
    }

}