package com.dizsun.weatherforecast;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dizsun.weatherforecast.util.CitiesUtil;
import com.dizsun.weatherforecast.util.CityMessage;
import com.dizsun.weatherforecast.util.FileHelper;
import com.dizsun.weatherforecast.util.PureNetUtil;
import com.dizsun.weatherforecast.util.WeatherUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Context context;                                //上下文，指向自己
    CitiesUtil citiesUtil;                          //操作城市集合的工具
    WeatherUtil weatherUtil;                        //操作天气的工具
    private ImageView imgWeather;                   //今日天气图标
    private TextView txtCityName;                   //当前查询的城市名
    private TextView txtCurrentDate;                //当前日期
    private TextView txtLunar;                      //当前农历日期
    private TextView txtCurrentWeek;                //当前周几
    private TextView txtCurrentBrief;               //当前天气简介
    private TextView txtCurrentTem;                 //当前温度
    private TextView txtDew;                        //当前湿度
    private TextView txtPM25;                       //当前PM2.5
    private TextView txtClothIndex;                 //穿衣指数
    private TextView txtColdIndex;                  //感冒指数
    private TextView txtExerciseIndex;              //锻炼指数
    private TextView txtUvIndex;                    //紫外线指数
    private TextView txtAirConditionIndex;          //空调指数
    private TextView txtWashCarIndex;               //洗车指数

    private FutureWeatherAdapter futureWeatherAdapter;
    private Dialog splashDialog;                    //应用开启时的缓冲页面
    private ListView futureWeatherList;             //未来天气简介列表
    /**
     * 处理线程回传的数据，根据<b>what</b>值来确定下一步更新那些组件
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == PureNetUtil.WHAT_CITY) {
                String value = ((String) msg.obj);
                citiesUtil.setValue(value);
                refreshPM25();
            } else if (msg.what == PureNetUtil.WHAT_PM25) {
                String value = (String) msg.obj;
                weatherUtil.initPm25(value);
                initWeatherUtil();
            } else if (msg.what == PureNetUtil.WHAT_WEATHER) {
                String value = (String) msg.obj;
                try {
                    weatherUtil.setValue(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                futureWeatherAdapter.setFutureBriefs(weatherUtil.getWeatherBriefs());
                futureWeatherList.setAdapter(futureWeatherAdapter);
                initMain();
                dismissSplashScreen();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        citiesUtil = new CitiesUtil(context);
        weatherUtil = new WeatherUtil(context, citiesUtil);

        imgWeather = (ImageView) findViewById(R.id.imgWeather);
        txtCityName = (TextView) findViewById(R.id.txtCityName);
        txtCurrentDate = (TextView) findViewById(R.id.txtCurrentDate);
        txtLunar = (TextView) findViewById(R.id.txtLunar);
        txtCurrentWeek = (TextView) findViewById(R.id.txtCurrentWeek);
        txtCurrentBrief = (TextView) findViewById(R.id.txtCurrentBrief);
        txtCurrentTem = (TextView) findViewById(R.id.txtCurrentTem);
        txtDew = (TextView) findViewById(R.id.txtDew);
        txtPM25 = (TextView) findViewById(R.id.txtPM25);
        txtClothIndex = (TextView) findViewById(R.id.txtClothIndex);
        txtColdIndex = (TextView) findViewById(R.id.txtColdIndex);
        txtExerciseIndex = (TextView) findViewById(R.id.txtExerciseIndex);
        txtUvIndex = (TextView) findViewById(R.id.txtUvIndex);
        txtAirConditionIndex = (TextView) findViewById(R.id.txtAirConditionIndex);
        txtWashCarIndex = (TextView) findViewById(R.id.txtWashCarIndex);
        futureWeatherList = (ListView) findViewById(R.id.futureWeatherList);
        /**
         * 设置单击监听器，单击图标舒心天气数据
         */
        imgWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initCitiesUtil();
            }
        });

        futureWeatherAdapter = new FutureWeatherAdapter(context);

        CityMessage cityMessage = new CityMessage();
        cityMessage.setProvince("陕西");
        cityMessage.setCity("西安");
        cityMessage.setDistrict("长安");
        citiesUtil.setmCityMessage(cityMessage);
        showSplashScreen();
        initCitiesUtil();
    }

    /**
     * 更新城市信息，然后刷新页面数据
     */
    private void initCitiesUtil() {
        if (FileHelper.existFile(context, FileHelper.CITIES_FILE)) {
            refreshPM25();
        } else {
            new Thread(new PureNetUtil(PureNetUtil.GET_CITIES, null, handler)).start();
        }
    }

    /**
     * 刷新pm25数据
     */
    private void refreshPM25() {
        new Thread(new PureNetUtil(PureNetUtil.GET_PM25, citiesUtil.getCityMessage().getCity(), handler)).start();
    }

    /**
     * 刷新天气数据
     */
    private void initWeatherUtil() {
        new Thread(new PureNetUtil(PureNetUtil.GET_WEATHER, citiesUtil.getCityMessage().getDistrict(), handler)).start();
    }

    /**
     * 设置各组件的值
     */
    private void initMain() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH", Locale.CHINA);
        int hour = Integer.parseInt(dateFormat.format(now));
        String dayOrNight = hour >= 7 && hour <= 19 ? "d" : "n";
        int imgDayId = context.getResources().getIdentifier(dayOrNight + nameConverter(weatherUtil.getRealTime().getWeather().getImg()), "drawable", context.getPackageName());
        imgWeather.setImageDrawable(context.getDrawable(imgDayId));

        txtCityName.setText(citiesUtil.getCityMessage().getProvince() + "-" + citiesUtil.getCityMessage().getCity() + "-" + citiesUtil.getCityMessage().getDistrict());
        txtCurrentDate.setText(weatherUtil.getPubdate());
        txtLunar.setText(weatherUtil.getRealTime().getMoon());
        txtCurrentWeek.setText(weatherUtil.getWeatherBriefs().get(0).getWeek());
        txtCurrentBrief.setText(weatherUtil.getRealTime().getWeather().getInfo());
        txtCurrentTem.setText(weatherUtil.getRealTime().getWeather().getTemperature() + "°C");
        txtDew.setText(weatherUtil.getRealTime().getWeather().getHumidity());
        if (weatherUtil.getPm25() != null)
            txtPM25.setText(weatherUtil.getPm25().getCurPm());
        else txtPM25.setText("-");

        txtClothIndex.setText(weatherUtil.getLifeIndex().getClothIndex());
        txtColdIndex.setText(weatherUtil.getLifeIndex().getColdIndex());
        txtExerciseIndex.setText(weatherUtil.getLifeIndex().getExerciseIndex());
        txtUvIndex.setText(weatherUtil.getLifeIndex().getUvIndex());
        txtAirConditionIndex.setText(weatherUtil.getLifeIndex().getAirConditionIndex());
        txtWashCarIndex.setText(weatherUtil.getLifeIndex().getWashCarIndex());
        //没有这一句时初始时scrollview会滑到最下面，这是让其滑到最上面，另外这一句必须在页面初始化后才能起作用，所以放在初始化语句的后面
        ((ScrollView)findViewById(R.id.myScrollView)).smoothScrollTo(0,0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissSplashScreen();
    }

    /**
     * 开启缓冲页面
     */
    private void showSplashScreen() {
        splashDialog = new Dialog(this, R.style.splash_screen);
        splashDialog.setContentView(R.layout.activity_splash);
        splashDialog.setCancelable(false);
        splashDialog.show();
    }

    /**
     * 关闭开头缓存页面
     */
    private void dismissSplashScreen() {
        if (splashDialog != null) {
            splashDialog.dismiss();
            splashDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.select_city:
                Intent intent = new Intent(context, SelectCity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

    /**
     * 接受SelectActivity回传的数据，填充到cityMessage中并刷新页面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String province = data.getStringExtra("province");
        String city = data.getStringExtra("city");
        String district = data.getStringExtra("district");
        citiesUtil.getCityMessage().setProvince(province);
        citiesUtil.getCityMessage().setCity(city);
        citiesUtil.getCityMessage().setDistrict(district);
        initCitiesUtil();
    }

    /**
     * 将name补位成两位数，如1补成01
     *
     * @param name
     * @return
     */
    private String nameConverter(String name) {
        if (name.length() == 1) return "0" + name;
        return name;
    }
}
