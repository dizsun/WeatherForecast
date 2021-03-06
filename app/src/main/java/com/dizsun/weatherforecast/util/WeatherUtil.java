package com.dizsun.weatherforecast.util;

import android.content.Context;

import com.dizsun.weatherforecast.util.beans.LifeIndex;
import com.dizsun.weatherforecast.util.beans.PM25;
import com.dizsun.weatherforecast.util.beans.RealTime;
import com.dizsun.weatherforecast.util.beans.WeatherBrief;

import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by sundiz on 16/11/29.
 */
public class WeatherUtil {

    private CitiesUtil citiesUtil;
    private String value;
    private Context context;
    // 天气更新日期
    private String pubdate;
    // 天气更新时间
    private String pubtime;
    //生活指数
    private LifeIndex lifeIndex;
    // 实时天气
    private RealTime realTime;
    // 今日及未来四日的天气概况
    private ArrayList<WeatherBrief> weatherBriefs;
    // pm2.5详细信息
    private PM25 pm25;

    public WeatherUtil(Context context, CitiesUtil citiesUtil) {
        this.context = context;
        this.citiesUtil = citiesUtil;
    }

    public WeatherUtil(Context context, String value, CitiesUtil citiesUtil) throws Exception {
        this.context = context;
        this.value = value;
        this.citiesUtil = citiesUtil;
        if (this.value != null && !this.value.isEmpty()) {
            JSONObject jsonObject = JSONObject.fromObject(value);
            int error_code = jsonObject.getInt("error_code");
            if (error_code == 0) {
                JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                JSONObject data = jsonObject1.getJSONObject("data");
                this.pubdate = data.getString("pubdate");
                this.pubtime = data.getString("pubtime");
                this.lifeIndex = new LifeIndex(data.getJSONObject("life").getJSONObject("info"));
                this.realTime = new RealTime(data.getJSONObject("realtime"));
                this.weatherBriefs = new ArrayList<>();
                for (Object weather : data.getJSONArray("weather")) {
                    JSONObject brief = (JSONObject) weather;
                    this.weatherBriefs.add(new WeatherBrief(brief.getJSONObject("info"), brief.getString("week"), brief.getString("date")));
                }
            } else if (error_code == 207302) {
                throw new WrongCityException();
            } else {
                throw new Exception();
            }

        } else {
            throw new Exception();
        }

    }

    /**
     * 设置从api获取的天气信息字符串，并解析更新天气信息
     * @param value 从api获取的天气信息字符串
     * @throws Exception
     */
    public void setValue(String value) throws Exception {
        if (this.value != null && !this.value.isEmpty())
            this.value = value;
        JSONObject jsonObject = JSONObject.fromObject(value);
        int error_code = jsonObject.getInt("error_code");
        if (error_code == 0) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            JSONObject data = jsonObject1.getJSONObject("data");
            this.pubdate = data.getString("pubdate");
            this.pubtime = data.getString("pubtime");
            this.lifeIndex = new LifeIndex(data.getJSONObject("life").getJSONObject("info"));
            this.realTime = new RealTime(data.getJSONObject("realtime"));
            this.weatherBriefs = new ArrayList<>();
            for (Object weather : data.getJSONArray("weather")) {
                JSONObject brief = (JSONObject) weather;
                this.weatherBriefs.add(new WeatherBrief(brief.getJSONObject("info"), brief.getString("week"), brief.getString("date")));
            }
        } else if (error_code == 207302) {
            throw new WrongCityException();
        } else {
            throw new Exception();
        }
    }

    public LifeIndex getLifeIndex() {
        return lifeIndex;
    }

    public String getPubdate() {
        SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTo = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            return sdfTo.format(sdfFrom.parse(pubdate));
        } catch (Exception e) {
            return pubdate;
        }
    }

    public String getPubtime() {
        return pubtime;
    }

    public RealTime getRealTime() {
        return realTime;
    }

    public ArrayList<WeatherBrief> getWeatherBriefs() {
        return weatherBriefs;
    }

    public PM25 getPm25() {
        return this.pm25;
    }

    /**
     * 因为只有市级行政区有pm2.5信息，所以pm2.5得专门设置，传入获取的从API获取的字符串解析出pm2.5信息
     * @param pmValue
     * @return
     */
    public PM25 initPm25(String pmValue) {
        try {
            JSONObject jsonObject = JSONObject.fromObject(pmValue).getJSONObject("result").getJSONObject("data").getJSONObject("pm25").getJSONObject("pm25");
            this.pm25 = new PM25(jsonObject);
        } catch (Exception e) {
            this.pm25 = null;
        }
        return this.pm25;
    }

}
