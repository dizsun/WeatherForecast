package com.dizsun.weatherforecast;

import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import com.dizsun.weatherforecast.util.CitiesUtil;
import com.dizsun.weatherforecast.util.beans.CityMessage;

/**
 * Created by sundiz on 16/12/18.
 */

public class OnCitySelectListener implements DialogInterface.OnClickListener {
    public CitiesUtil citiesUtil;
    public ArrayAdapter<String> selectAdapter;
    public String selectedProvince, selectedCity, selectedDistrict;
    //判断当前列表内容时用到的标记常量
    final int STATE_PROVINCE = 0;
    final int STATE_CITY = 1;
    final int STATE_DISTRICT = 3;
    //当前列表显示状态
    int state = 0;
    public boolean isCompleted=false;
    CityMessage cityMessage=new CityMessage();
    @Override
    public void onClick(DialogInterface dialogInterface, int position) {
        switch (state){
            case STATE_PROVINCE:
                selectedProvince = citiesUtil.getProvinces().get(position);
                selectAdapter.clear();
                selectAdapter.addAll(citiesUtil.getCities(selectedProvince));
                selectAdapter.notifyDataSetChanged();
                state = STATE_CITY;
                cityMessage.setProvince(selectedProvince);
                break;
            case STATE_CITY:
                selectedCity = citiesUtil.getCities(selectedProvince).get(position);
                selectAdapter.clear();
                selectAdapter.addAll(citiesUtil.getDistrics(selectedProvince, selectedCity));
                selectAdapter.notifyDataSetChanged();
                state = STATE_DISTRICT;
                cityMessage.setCity(selectedCity);
                break;
            case STATE_DISTRICT:
                selectedDistrict = citiesUtil.getDistrics(selectedProvince, selectedCity).get(position);
                cityMessage.setDistrict(selectedDistrict);
                isCompleted=true;
                break;
        }
    }
}
