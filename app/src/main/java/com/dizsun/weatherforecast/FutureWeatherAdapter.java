package com.dizsun.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dizsun.weatherforecast.util.beans.WeatherBrief;

import java.util.ArrayList;

/**
 * Created by sundiz on 16/12/15.
 */

public class FutureWeatherAdapter extends BaseAdapter{
    //今天和未来四天天气列表
    private ArrayList<WeatherBrief> futureBriefs;
    private final Context context;

    public FutureWeatherAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return futureBriefs.size();
    }

    @Override
    public Object getItem(int i) {
        return futureBriefs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView != null ? convertView : createView(parent);
        WeatherBrief weatherBrief = futureBriefs.get(position);
        int imgDayId = context.getResources().getIdentifier("d"+nameConverter(weatherBrief.getDay().getCode()),"drawable",context.getPackageName());
        int imgNightId = context.getResources().getIdentifier("n"+nameConverter(weatherBrief.getNight().getCode()),"drawable",context.getPackageName());
        //白天天气图标
        ((ImageView)view.findViewById(R.id.imgDay)).setImageDrawable(context.getDrawable(imgDayId));
        //夜间天气图标
        ((ImageView)view.findViewById(R.id.imgNight)).setImageDrawable(context.getDrawable(imgNightId));
        ((TextView)view.findViewById(R.id.txtDate)).setText(weatherBrief.getDate());
        ((TextView)view.findViewById(R.id.txtWeek)).setText(weatherBrief.getWeek());
        //白天天气简介
        ((TextView)view.findViewById(R.id.txtDayBrief)).setText(weatherBrief.getDay().getWeather());
        ((TextView)view.findViewById(R.id.txtNightBrief)).setText(weatherBrief.getNight().getWeather());
        ((TextView)view.findViewById(R.id.txtHighTem)).setText(weatherBrief.getDay().getHighTemp()+"°C");
        ((TextView)view.findViewById(R.id.txtLowTem)).setText(weatherBrief.getNight().getHighTemp()+"°C");
        return view;
    }

    private View createView(ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflatedView = inflater.inflate(R.layout.future_overview, parent, false);
        return inflatedView;
    }

    /**
     * 设置列表内容并更新列表
     * @param futureBriefs
     */
    public void setFutureBriefs(ArrayList<WeatherBrief> futureBriefs) {
        this.futureBriefs = futureBriefs;
        notifyDataSetChanged();
    }

    /**
     * 将图片名字补位为合适的形式，如1补位为01
     * @param name
     * @return
     */
    private String nameConverter(String name){
        if(name.length()==1) return "0"+name;
        return name;
    }
}
