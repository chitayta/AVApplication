package com.tma.tctay.models;

/**
 * Created by tctay on 6/15/2017.
 */

public class ReceivedData {

    private String systemUID;
    private Temperature temperatureData;
    private Motion motionData;

    public String getSystemUID() {
        return systemUID;
    }

    public Temperature getTemperatureData() {
        return temperatureData;
    }

    public Motion getMotionData() {
        return motionData;
    }

    public ReceivedData(String systemUID, Temperature temperatureData, Motion motionData) {
        this.systemUID = systemUID;
        this.temperatureData = temperatureData;
        this.motionData = motionData;
    }

    public ReceivedData(String systemUID, Temperature temperatureData) {
        this.systemUID = systemUID;
        this.temperatureData = temperatureData;
    }

    public ReceivedData(String systemUID, Motion motionData) {
        this.systemUID = systemUID;
        this.motionData = motionData;
    }
}
