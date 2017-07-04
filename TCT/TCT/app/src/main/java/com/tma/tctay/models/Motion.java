package com.tma.tctay.models;

/**
 * Created by tctay on 6/15/2017.
 */

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

public class Motion {

    private String motionData;
    private Long motionTimeStamp;
    private LocalDateTime localDateTimeTimeStamp;
    private String motionTimeStampString;

    public String getMotionData() {
        return motionData;
    }

    public Long getMotionTimeStamp() {
        return motionTimeStamp;
    }

    public LocalDateTime getLocalDateTimeTimeStamp() {
        return localDateTimeTimeStamp;
    }

    public String getMotionTimeStampString() {
        return motionTimeStampString;
    }

    public Motion(String motionData, Long motionTimeStamp) {
        this.motionData = motionData;
        this.motionTimeStamp = motionTimeStamp;
        //Instant instant = Instant.ofEpochMilli(motionTimeStamp);

        Date date = new Date(motionTimeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // give a timezone reference for formating (see comment at the bottom
        motionTimeStampString = sdf.format(date);
    }

}
