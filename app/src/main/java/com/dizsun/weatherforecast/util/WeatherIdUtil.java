package com.dizsun.weatherforecast.util;

import com.dizsun.weatherforecast.util.wid.WeatherIdBean;
import com.dizsun.weatherforecast.util.wid.WeatherIdResultBean;
import net.sf.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 从Path路径下的json文件中获取weatherid列表
 *
 * @author sundiz
 */
public class WeatherIdUtil {
    private String path;
    private String result = null;
    private ArrayList<WeatherIdBean> wids = null;

    public WeatherIdUtil(String path) {
        if (path != null && !path.isEmpty()) this.path = path;
        else this.path = "wids.json";
        getWidsJSONString(this.path);
//        String wids=PureNetUtil.get("http://v.juhe.cn/weather/uni?key=739bfe395c42a1ddf500b2cf65c02cce");
    }

    /**
     * 获取weatherid列表
     *
     * @return 返回WeatherIdBean列表, 每个项目包含wid, weather
     */
    public ArrayList<WeatherIdBean> getWeatherIds() {
        if (this.result == null) return null;
        if (this.wids == null) {
            Map<String, Class<WeatherIdBean>> map = new HashMap<>();
            map.put("result", WeatherIdBean.class);
            JSONObject jsonObject = JSONObject.fromObject(this.result);
            WeatherIdResultBean weatherIdResult = (WeatherIdResultBean) JSONObject.toBean(jsonObject, WeatherIdResultBean.class);
            this.wids = weatherIdResult.getResult();
        }
        return this.wids;
    }

    /**
     * 从本地json文件中获取全部内容保存在result中
     *
     * @param path 本地json文件路径
     */
    private void getWidsJSONString(String path) {
        if (this.result == null) {
            this.result = "";
            try (FileInputStream fileInputStream = new FileInputStream(path);){
                byte[] buff = new byte[1024];
                int len;
                while ((len = fileInputStream.read(buff)) != -1) {
                    this.result += new String(buff, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
                this.result = null;
            }
        }
    }
}
