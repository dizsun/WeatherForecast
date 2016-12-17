package com.dizsun.weatherforecast.util.weather;

import net.sf.json.JSONObject;

/**
 * Created by sundiz on 16/11/29.
 */
public class Wind {
    private String direct;
    private String power;
    private String offset;
    private String windspeed;

    public Wind() {
    }

    public Wind(JSONObject value) {
        this.direct = value.getString("direct");
        this.power = value.getString("power");
        this.offset = value.getString("offset");
        this.windspeed = value.getString("windspeed");
    }

    public String getDirect() {
        return direct;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public void setDirect(String direct) {
        this.direct = direct;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}
