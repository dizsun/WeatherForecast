package com.dizsun.weatherforecast.util.beans;

import net.sf.json.JSONObject;

/**
 * Created by sundiz on 16/11/29.
 */
public class Weather {
    //天气信息
    private String temperature;
    private String humidity;
    private String info;
    private String img;

    public Weather() {
    }

    public Weather(JSONObject value) {
        this.temperature = value.getString("temperature");
        this.humidity = value.getString("humidity");
        this.info = value.getString("info");
        this.img = value.getString("img");
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getImg() {
        if(img.equals("33"))return "53";
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
