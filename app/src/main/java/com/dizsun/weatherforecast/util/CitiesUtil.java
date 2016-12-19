package com.dizsun.weatherforecast.util;

import android.content.Context;
import android.util.Log;

import com.dizsun.weatherforecast.util.beans.CityMessage;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 从路径为path的json文件中获取城市列表
 *
 * @author sundiz
 */

public class CitiesUtil {
    private String mValue = null;
    private ArrayList<CityMessage> cityMessages = null;
    private HashSet<String> mCities;
    private Context mContext;

    private CityMessage cityMessage;
    private HashMap<String, HashMap<String, ArrayList<String>>> mCityTable = null;


    public CitiesUtil(Context context, String value) throws Exception{
        this.mValue = value;
        this.mContext = context;
        try {
            FileHelper.writeData(this.mContext,FileHelper.CITIES_FILE,this.mValue);
        }catch (Exception e){
            System.err.println("文件写入失败");
            throw new Exception();
        }
    }

    public CitiesUtil(Context context) {
        this.mContext = context;
        if(mValue==null &&existCityFile()){
            try {
                this.mValue=FileHelper.readData(this.mContext,FileHelper.CITIES_FILE);
                if(!getCityTable()) throw new Exception("城市列表初始化失败！");
            }catch (Exception e){
                System.err.println("文件读取失败");
            }
        }
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue){
        if (mValue != null) {
            this.mValue = mValue;
            try {
                FileHelper.writeData(this.mContext,FileHelper.CITIES_FILE,this.mValue);
                if(!getCityTable()) throw new Exception("城市列表初始化失败！");
            }catch (Exception e){
                System.err.println(e.getMessage());
            }
        }
    }

    public CityMessage getCityMessage() {
        return cityMessage;
    }

    public void setCityMessage(CityMessage mCityMessage) {
        this.cityMessage = mCityMessage;
    }

    private boolean getCityTable() {
        if (this.mValue == null) return false;
        if (this.cityMessages == null && getCityBeans() == null) return false;
        if (mCityTable == null) {
            mCityTable=new HashMap<>();
            for (CityMessage mCityMessage : cityMessages) {
//                Log.d("WF",cityMessage.getProvince());
                if (mCityTable.keySet().contains(mCityMessage.getProvince())){
                    HashMap<String, ArrayList<String>> prov=mCityTable.get(mCityMessage.getProvince());
                    if(prov.keySet().contains(mCityMessage.getCity())){
                        ArrayList<String>dis = prov.get(mCityMessage.getCity());
                        if(!dis.contains(mCityMessage.getDistrict())) {
                            dis.add(mCityMessage.getDistrict());
                            prov.put(mCityMessage.getCity(),dis);
                        }
                    }else {
                        ArrayList<String> dis=new ArrayList<>();
                        dis.add(mCityMessage.getDistrict());
                        prov.put(mCityMessage.getCity(),dis);
                    }
                    mCityTable.put(mCityMessage.getProvince(),prov);
                }else {
                    ArrayList<String> dis=new ArrayList<>();
                    HashMap<String,ArrayList<String>> cities=new HashMap<>();
                    dis.add(mCityMessage.getDistrict());
                    cities.put(mCityMessage.getCity(),dis);
                    mCityTable.put(mCityMessage.getProvince(),cities);
                }
            }

        }
        return mCityTable != null;

    }


    /**
     * 获取城市列表
     *
     * @return 返回城市列表, 每个项目都是CityBean, 包含id, 省份, 城市名, 区/县名
     */
    public ArrayList<CityMessage> getCityBeans() {
        if (this.mValue == null) return null;
        if (this.cityMessages == null) {
            JSONObject citiesJsonObject = JSONObject.fromObject(this.mValue);
            JSONArray jsonArray = JSONArray.fromObject(citiesJsonObject.getJSONArray("result"));
            ArrayList<CityMessage> cbs=new ArrayList<>();
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                cbs.add(new CityMessage(jsonObject));
            }
            this.cityMessages = cbs;
        }
        return this.cityMessages;
    }

    /**
     * 获取所有省份列表
     *
     * @return
     */
    public ArrayList<String> getProvinces() {
        if(this.mCityTable==null && !getCityTable()) return null;
        return new ArrayList<String>(mCityTable.keySet());

    }

    /**
     * 获取某省的所有市
     *
     * @param province
     * @return
     */
    public ArrayList<String> getCities(String province) {
        if(this.mCityTable==null && !getCityTable()) return null;
        return new ArrayList<>(mCityTable.get(province).keySet());
    }

    /**
     * 获取县/区列表
     *
     * @param province
     * @param city
     * @return
     */
    public ArrayList<String> getDistrics(String province, String city) {
        if(this.mCityTable==null && !getCityTable()) return null;
        return mCityTable.get(province).get(city);
    }

    /**
     * 判断配置文件是否存在
     *
     * @return
     */
    public boolean existCityFile() {
        return FileHelper.existFile(this.mContext, FileHelper.CITIES_FILE);
    }


    /**
     * 判断传入的地点是否是城市,而不是区/县或者其他未含在本列表中的城市
     *
     * @param place 要查询的地区名
     * @return
     */
    public boolean isCity(String place) {
        if (this.cityMessages == null) {
            if (getCityBeans() == null) return false;
        }
        if (mCities == null) {
            mCities = new HashSet<>();
            for (CityMessage cityMessage : cityMessages) {
                mCities.add(cityMessage.getCity());
            }
        }
        return mCities.contains(place);
    }
}
