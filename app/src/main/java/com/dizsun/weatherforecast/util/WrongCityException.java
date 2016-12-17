package com.dizsun.weatherforecast.util;

/**
 * Created by sundiz on 16/12/14.
 */
public class WrongCityException extends Exception {
    @Override
    public String getMessage() {
        return "未找到输入城市";
    }

    @Override
    public String toString() {
        return "未找到输入城市";
    }

    @Override
    public void printStackTrace() {
        System.out.println("未找到输入城市");
    }
}
