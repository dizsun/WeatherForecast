package com.dizsun.weatherforecast.util.city;


import net.sf.json.JSONObject;

/**
 * Created by sundiz on 16/11/29.
 */
public class CityBean {
    private String id;
    private String province;
    private String city;
    private String district;

    public CityBean() {
    }

    public CityBean(JSONObject value){
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
