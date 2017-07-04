package com.tma.tctay.utility;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by tctay on 6/19/2017.
 */

public class AirVantageAPICaller
{

    public void getString(String url, final VolleyCallback callback) {
        JsonObjectRequest jsObjRequestData = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Jsresult = response;
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

}
