package com.dizsun.weatherforecast.util.beans;


import net.sf.json.JSONObject;

/**
 * Created by sundiz on 16/11/29.
 */
public class CityMessage {
    //城市编号
    private String id;
    //省份
    private String province;
    //市
    private String city;
    //县(区)
    private String district;

    public CityMessage() {
    }

    public CityMessage(JSONObject value){
        JSONObject jsonObject = JSONObject.fromObject(value);
        id=jsonObject.getString("id");
        province=jsonObject.getString("province");
        city=jsonObject.getString("city");
        district=jsonObject.getString("district");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

}
