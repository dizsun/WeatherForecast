package com.dizsun.weatherforecast.util.beans;

import net.sf.json.JSONArray;

/**
 * Created by sundiz on 16/11/29.
 */
public class WeatherInfo {
    //天气图片代码
    private String code;
    //天气简介
    private String weather;
    //温度
    private String highTemp;
    //风向
    private String windDirect;
    //风力
    private String windStrength;

    public WeatherInfo(JSONArray jsonArray) {
        this.code=jsonArray.getString(0);
        this.code=code.equals("33")?"53":code;
        this.weather=jsonArray.getString(1);
        this.highTemp=jsonArray.getString(2);
        this.windDirect=jsonArray.getString(3);
        this.windStrength=jsonArray.getString(4);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(String highTemp) {
        this.highTemp = highTemp;
    }

    public String getWindDirect() {
        return windDirect;
    }

    public void setWindDirect(String windDirect) {
        this.windDirect = windDirect;
    }

    public String getWindStrength() {
        return windStrength;
    }

    public void setWindStrength(String windStrength) {
        this.windStrength = windStrength;
    }
}
