package com.tma.tctay.asynctask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.tma.tctay.models.AccessToken;
import com.tma.tctay.utility.AirVantageMQTTConfiguration;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by tctay on 7/14/2017.
 */

public class SubscribeMessageTopic extends AsyncTask<Void, Boolean, Boolean>{

    private MqttClient client;
    private MqttConnectOptions options;
    private Context appContext;
    private Activity appActivity;
    private String deviceSerialNumber;
    private String systemUid;
    private AccessToken accessToken;

    public SubscribeMessageTopic(Context appContext, String deviceSerialNumber, String systemUid, AccessToken accessTokenResponse, Activity appActivity)
    {
        this.appContext = appContext;
        this.deviceSerialNumber = deviceSerialNumber;
        this.systemUid = systemUid;
        this.accessToken = accessTokenResponse;
        this.appActivity = appActivity;
    }

    public void refreshData()
    {
        (new GetDataFromCloudTask(systemUid, accessToken, appContext, appActivity)).execute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {

            client = new MqttClient(AirVantageMQTTConfiguration.BROKER, AirVantageMQTTConfiguration.getClienId(deviceSerialNumber), AirVantageMQTTConfiguration.PERSISTENCE);
            client.setCallback(new MqttCallback(){
                @Override
                public void connectionLost(Throwable cause) {
                    cause.printStackTrace();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.err.println("Message arrived. Topic: " + topic + " Message: " + message.toString());
                    Thread.sleep(3000);
                    refreshData();
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
            options = new MqttConnectOptions();
            options.setUserName(deviceSerialNumber);
            options.setPassword("admin_12Admin".toCharArray());
            options.setCleanSession(true);

            client.connect(options);
            if (!client.isConnected())
                return false;
            client.subscribe(AirVantageMQTTConfiguration.getMessageTopic(deviceSerialNumber));

        } catch (MqttException e) {
            System.out.println("KOK");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean isSuccessful) {
        if (isSuccessful) {
            Toast toast = Toast.makeText(appContext, "Subscribed to MQTT messages topic!", Toast.LENGTH_LONG);
            TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
            toastMessage.setTextColor(Color.GREEN);
            toast.show();
        }
        else
        {
            Toast toast = Toast.makeText(appContext, "Fail to subscribed to MQTT messages topic!", Toast.LENGTH_LONG);
            TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
            toastMessage.setTextColor(Color.RED);
            toast.show();
        }
    }
}
