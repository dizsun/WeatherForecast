package com.dizsun.weatherforecast.util.beans;

import net.sf.json.JSONObject;

/**
 * Created by sundiz on 16/11/29.
 */
public class RealTime {
    private JSONObject realTime;
    private String city_code;
    private String date;
    private String time;
    private int week;
    //农历日期
    private String moon;
    private Weather weather;
    private Wind wind;

    public RealTime(JSONObject realTime) {
        this.realTime = realTime;
        this.city_code = realTime.getString("city_code");
        this.date=realTime.getString("date");
        this.time=realTime.getString("time");
        this.week=realTime.getInt("week");
        this.moon=realTime.getString("moon");
        this.weather=new Weather(realTime.getJSONObject("weather"));
        this.wind=new Wind(realTime.getJSONObject("wind"));
    }

    public String getCity_code() {
        return city_code;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getWeek() {
        return week;
    }

    public String getMoon() {
        return moon;
    }

    public Weather getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }
}
