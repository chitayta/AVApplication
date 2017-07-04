package com.tma.tctay.asynctask;

/**
 * Created by tctay on 7/4/2017.
 */


//public class GetSystemUidTask extends AsyncTask<Void, Void, String> {
//
//    private String access_token;
//    private String systenName;
//
//    public GetSystemUidTask(String access_token, String systemName)
//    {
//        this.access_token = access_token;
//        this.systenName = systemName;
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//        String dataUrl = "https://eu.airvantage.net/api/v1/systems?name="+systenName+"&fields=uid,name,gateway,subscriptions&access_token="+this.access_token;
//
//        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//
//        RequestFuture<JSONObject> future = RequestFuture.newFuture();
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET ,dataUrl, new JSONObject(), future, future);
//
//        mRequestQueue.add(request);
//
//        try {
//            System.out.println("Getting response from request...");
//
//            JSONObject response = future.get(); // this will block
//
//            System.out.println("Response from request: " + response);
//
//            JSONArray items = response.getJSONArray("items");
//
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onPostExecute(final String systemUid) {
//    }
//}
