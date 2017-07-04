package com.tma.tctay.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tctay on 6/15/2017.
 */

public class Temperature {

    private Float temperatureData;
    private Long temperatureTimeStamp;
    private String temperatureTimeStampString;

    public Float getTemperatureData() {
        return temperatureData;
    }

    public Long getTemperatureTimeStamp() {
        return temperatureTimeStamp;
    }

    public String getTemperatureTimeStampString() {
        return temperatureTimeStampString;
    }

    public Temperature(Float temperatureData, Long temperatureTimeStamp) {
        this.temperatureData = temperatureData;
        this.temperatureTimeStamp = temperatureTimeStamp;

        Date date = new Date(temperatureTimeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // give a timezone reference for formating (see comment at the bottom
        temperatureTimeStampString = sdf.format(date);
    }
}
