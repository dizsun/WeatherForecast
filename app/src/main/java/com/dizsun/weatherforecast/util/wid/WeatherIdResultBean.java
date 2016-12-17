package com.dizsun.weatherforecast.util.wid;

import java.util.ArrayList;

/**
 * Created by sundiz on 16/11/29.
 */
public class WeatherIdResultBean {
    private String resultcode;
    private String reason;
    private ArrayList<WeatherIdBean> result;
    private int error_code;

    public String getResultcode() {
        return resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ArrayList<WeatherIdBean> getResult() {
        return result;
    }

    public void setResult(ArrayList<WeatherIdBean> result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}
