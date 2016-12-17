package com.dizsun.weatherforecast.util.weather;

import net.sf.json.JSONObject;

/**
 * Created by sundiz on 16/11/29.
 */
public class PM25 {
    /**
     "pm25":{
     "curPm":"164",
     "pm25":"126",
     "pm10":"201",
     "level":4,
     "quality":"中度污染",
     "des":"对污染物比较敏感的人群，例如儿童和老年人、呼吸道疾病或心脏病患者，以及喜爱户外活动的人，他们的健康状况会受到影响，但对健康人群基本没有影响。"
     }
     */
    private String curPm;
    private String pm25;
    private String pm10;
    private int level;
    private String quality;
    private String des;

    public PM25() {
    }
    public PM25(JSONObject value) {
        curPm=value.getString("curPm");
        pm25=value.getString("pm25");
        pm10=value.getString("pm10");
        level=value.getInt("level");
        quality=value.getString("quality");
        des=value.getString("des");
    }

    public String getCurPm() {
        return curPm;
    }

    public void setCurPm(String curPm) {
        this.curPm = curPm;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
