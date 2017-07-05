package com.tma.tctay.models;

/**
 * Created by tctay on 6/15/2017.
 */

public class ReceivedLastDataPoint {
    private Temperature temperatureData;
    private Motion motionData;

    public Temperature getTemperatureData() {
        return temperatureData;
    }

    public Motion getMotionData() {
        return motionData;
    }

    public ReceivedLastDataPoint(Temperature temperatureData, Motion motionData) {
        this.temperatureData = new Temperature(temperatureData.getTemperatureData(), temperatureData.getTemperatureTimeStamp());
        this.motionData = new Motion( motionData.getMotionData(),motionData.getMotionTimeStamp());
    }

    public ReceivedLastDataPoint(Temperature temperatureData) {
        this.temperatureData = new Temperature(temperatureData.getTemperatureData(),temperatureData.getTemperatureTimeStamp());
    }

    public ReceivedLastDataPoint(Motion motionData) {
        this.motionData = new Motion(motionData.getMotionData(), motionData.getMotionTimeStamp());
    }
}
