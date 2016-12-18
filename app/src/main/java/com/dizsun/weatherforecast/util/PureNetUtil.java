package com.dizsun.weatherforecast.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 网络访问工具类
 * @author dizsun
 *
 */
public class PureNetUtil implements Runnable {
    //运行选项，获取那种信息
    public static final int GET_CITIES=0;
    public static final int GET_WEATHER=1;
    public static final int GET_PM25=3;
    //返回的what值
    public static final int WHAT_CITY =0x123;
    public static final int WHAT_WEATHER =0x234;
    public static final int WHAT_PM25 =0x345;

    private static final String CITIES_URL="http://v.juhe.cn/weather/citys?key=739bfe395c42a1ddf500b2cf65c02cce";
    private static final String WEATHER_URL="http://op.juhe.cn/onebox/weather/query?cityname=";
    private static final String KEY="01d9e875539fbe6355d7ee8c9d2320de";
    private int what;
    private String request;
    private Handler handler;

    public PureNetUtil(int param, String city, Handler handler) {
        if(param==GET_CITIES){
            what =WHAT_CITY;
            request=CITIES_URL;
        }else if(param==GET_WEATHER){
            what =WHAT_WEATHER;
            request=WEATHER_URL+city+"&key="+KEY;
        }else if(param==GET_PM25){
            what=WHAT_PM25;
            request=WEATHER_URL+city+"&key="+KEY;
        }
        this.handler=handler;
    }

    /**
     * get方法从传入的url中获取json字符串
     * @param url 网络地址
     * @return 返回网络数据
     */
    private   String get(String url){
        String ans = null;
        try {
            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.connect();
            int recode = conn.getResponseCode();
            BufferedReader reader;
            if (recode == 200) {
                //Returns an input stream that reads from this open connection
                InputStream in = conn.getInputStream();
                //对输入流进行封装
                reader = new BufferedReader(new InputStreamReader(in));
                if ((ans = reader.readLine()) != null) {
                    return ans;
                }
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ans;
    }

    @Override
    public void run() {
        String value=get(request);
        Message message=new Message();
        message.what= this.what;
        message.obj=value;
        handler.sendMessage(message);
    }
}