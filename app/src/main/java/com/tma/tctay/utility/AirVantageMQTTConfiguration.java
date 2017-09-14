package com.tma.tctay.utility;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created by tctay on 7/14/2017.
 */

public class AirVantageMQTTConfiguration {

    public static final String BROKER = "tcp://eu.airvantage.net:1883";
    public static final MemoryPersistence PERSISTENCE = new MemoryPersistence();
    public static final int QOS = 2;
    public static String getClienId (String deviceSerialNumber)
    {
        return deviceSerialNumber + "-subscriber";
    }
    public static String getMessageTopic(String deviceSerialNumber)
    {
        return deviceSerialNumber+"/messages/json";
    }

}
