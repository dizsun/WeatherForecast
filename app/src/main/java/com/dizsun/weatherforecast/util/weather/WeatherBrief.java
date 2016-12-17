package com.dizsun.weatherforecast.util.weather;

import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by sundiz on 16/11/29.
 */
public class WeatherBrief  {
    private JSONObject weathers;

    private WeatherInfo day;
    private WeatherInfo dawn;
    private WeatherInfo night;
    private String week;
    private String date;

    public WeatherBrief(JSONObject weathers,String week,String date) {
        this.weathers = weathers;
        this.week="星期"+week;
        this.date=date;
        this.day=new WeatherInfo(weathers.getJSONArray("day"));
        this.night=new WeatherInfo(weathers.getJSONArray("night"));
        if(weathers.containsKey("dawn")){
            this.dawn=new WeatherInfo(weathers.getJSONArray("dawn"));
        }
    }

    public String getDate() {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            return sdfTo.format(sdfFrom.parse(date));
        } catch (Exception e) {
            return date;
        }
    }

    public String getWeek() {
        return week;
    }

    public WeatherInfo getDay() {
        return day;
    }

    public WeatherInfo getDawn() {
        return dawn;
    }

    public WeatherInfo getNight() {
        return night;
    }
}
