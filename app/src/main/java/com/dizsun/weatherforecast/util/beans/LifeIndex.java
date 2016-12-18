package com.dizsun.weatherforecast.util.beans;

import net.sf.json.JSONObject;

/**
 * Created by sundiz on 16/12/15.
 */

public class LifeIndex {
    //生活指数：穿衣指数、感冒指数、空调指数、洗车指数、锻炼指数、紫外线指数
    private String clothIndex;
    private String coldIndex;
    private String airConditionIndex;
    private String washCarIndex;
    private String exerciseIndex;
    private String uvIndex;

    public LifeIndex(JSONObject value) {
        clothIndex=value.getJSONArray("chuanyi").getString(0)+","+value.getJSONArray("chuanyi").getString(1);
        coldIndex=value.getJSONArray("ganmao").getString(0)+","+value.getJSONArray("ganmao").getString(1);
        airConditionIndex=value.getJSONArray("kongtiao").getString(0)+","+value.getJSONArray("kongtiao").getString(1);
        washCarIndex=value.getJSONArray("xiche").getString(0)+","+value.getJSONArray("xiche").getString(1);
        exerciseIndex=value.getJSONArray("yundong").getString(0)+","+value.getJSONArray("yundong").getString(1);
        uvIndex=value.getJSONArray("ziwaixian").getString(0)+","+value.getJSONArray("ziwaixian").getString(1);
    }

    public String getClothIndex() {
        return clothIndex;
    }

    public String getColdIndex() {
        return coldIndex;
    }

    public String getAirConditionIndex() {
        return airConditionIndex;
    }

    public String getWashCarIndex() {
        return washCarIndex;
    }

    public String getExerciseIndex() {
        return exerciseIndex;
    }

    public String getUvIndex() {
        return uvIndex;
    }
}
