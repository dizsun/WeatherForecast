package com.dizsun.weatherforecast;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.dizsun.weatherforecast.util.CitiesUtil;
import com.dizsun.weatherforecast.util.CityMessage;

public class SelectCity extends ListActivity {
    private static final int STATE_PROVINCE = 0;
    private static final int STATE_CITY = 1;
    private static final int STATE_DISTRICT = 3;
    private int state=0;
    private CitiesUtil citiesUtil;
    private TextView txtSelectedProvince;
    private TextView txtSelectedCity;
    private TextView txtSelectedDistrict;
    private Button btnOK;
    private Button btnReturn;
    private ArrayAdapter<String> adapter;
    private String province;
    private String city;
    private String district;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        Context context = getParent();
        citiesUtil = new CitiesUtil(this);

        txtSelectedProvince = (TextView) findViewById(R.id.selectedProvince);
        txtSelectedCity = (TextView) findViewById(R.id.selectedCity);
        txtSelectedDistrict = (TextView) findViewById(R.id.selectedDistrict);
        btnOK = (Button) findViewById(R.id.selectOK);
        btnReturn = (Button) findViewById(R.id.selectReturn);

        adapter = new ArrayAdapter<>(this,R.layout.select_city_item,R.id.city_item,citiesUtil.getProvinces());
        setListAdapter(adapter);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("province",txtSelectedProvince.getText().toString());
                intent.putExtra("city",txtSelectedCity.getText().toString());
                intent.putExtra("district",txtSelectedDistrict.getText().toString());
                setResult(100,intent);
                finish();
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (state){
                    case STATE_PROVINCE:
                        txtSelectedProvince.setText("");
                        txtSelectedCity.setText("");
                        txtSelectedDistrict.setText("");
                        break;
                    case STATE_CITY:
                        adapter.clear();
                        adapter.addAll(citiesUtil.getProvinces());
                        adapter.notifyDataSetChanged();
                        txtSelectedCity.setText("");
                        state=STATE_PROVINCE;
                        break;
                    case STATE_DISTRICT:
                        adapter.clear();
                        adapter.addAll(citiesUtil.getCities(province));
                        adapter.notifyDataSetChanged();
                        txtSelectedDistrict.setText("");
                        state=STATE_CITY;
                        break;
                }
            }

        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        switch (state){
            case STATE_PROVINCE:
                province = citiesUtil.getProvinces().get(position);
                adapter.clear();
                adapter.addAll(citiesUtil.getCities(province));
                adapter.notifyDataSetChanged();
                txtSelectedProvince.setText(province);
                txtSelectedCity.setText("");
                txtSelectedDistrict.setText("");
                state=STATE_CITY;
                break;
            case STATE_CITY:
                city=citiesUtil.getCities(province).get(position);
                adapter.clear();
                adapter.addAll(citiesUtil.getDistrics(province,city));
                adapter.notifyDataSetChanged();
                txtSelectedCity.setText(city);
                state=STATE_DISTRICT;
                break;
            case STATE_DISTRICT:
                district=citiesUtil.getDistrics(province,city).get(position);
                txtSelectedDistrict.setText(district);
                break;
        }
    }

}
