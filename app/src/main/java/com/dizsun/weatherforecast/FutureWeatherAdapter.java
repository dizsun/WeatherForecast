package com.dizsun.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dizsun.weatherforecast.util.weather.WeatherBrief;

import java.util.ArrayList;

/**
 * Created by sundiz on 16/12/15.
 */

public class FutureWeatherAdapter extends BaseAdapter{
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
        ((ImageView)view.findViewById(R.id.imgDay)).setImageDrawable(context.getDrawable(imgDayId));
        ((ImageView)view.findViewById(R.id.imgNight)).setImageDrawable(context.getDrawable(imgNightId));
        ((TextView)view.findViewById(R.id.txtDate)).setText(weatherBrief.getDate());
        ((TextView)view.findViewById(R.id.txtWeek)).setText(weatherBrief.getWeek());
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

    public void setFutureBriefs(ArrayList<WeatherBrief> futureBriefs) {
        this.futureBriefs = futureBriefs;
        notifyDataSetChanged();
    }

    private String nameConverter(String name){
        if(name.length()==1) return "0"+name;
        return name;
    }
}
