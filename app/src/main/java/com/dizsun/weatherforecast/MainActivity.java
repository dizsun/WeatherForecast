package com.dizsun.weatherforecast;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dizsun.weatherforecast.util.CitiesUtil;
import com.dizsun.weatherforecast.util.WrongCityException;
import com.dizsun.weatherforecast.util.beans.CityMessage;
import com.dizsun.weatherforecast.util.PureNetUtil;
import com.dizsun.weatherforecast.util.WeatherUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Context context;                                //上下文，指向自己
    CitiesUtil citiesUtil;                          //操作城市集合的工具
    WeatherUtil weatherUtil;                        //操作天气的工具
    private SwipeRefreshLayout refreshLayout;
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
            if (msg.what == PureNetUtil.WHAT_PM25) {
                String value = (String) msg.obj;
                weatherUtil.initPm25(value);
                initWeatherUtil();
            } else if (msg.what == PureNetUtil.WHAT_WEATHER) {
                String value = (String) msg.obj;
                try {
                    weatherUtil.setValue(value);
                    futureWeatherAdapter.setFutureBriefs(weatherUtil.getWeatherBriefs());
                    futureWeatherList.setAdapter(futureWeatherAdapter);
                    initMain();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "天气信息获取失败！", Toast.LENGTH_LONG).show();
                }
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
        showSplashScreen();
        context = this;
        try {
            citiesUtil = new CitiesUtil(context);
        } catch (WrongCityException e) {
            Toast.makeText(context, "城市列表初始化失败！", Toast.LENGTH_LONG).show();
            finish();
        }
        weatherUtil = new WeatherUtil(context, citiesUtil);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        //实例化各组件
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
        // 设置单击监听器，单击图标刷新天气数据
        imgWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginRefresh();
            }
        });

        futureWeatherAdapter = new FutureWeatherAdapter(context);
        //初始化时设为西安长安区的天气
        CityMessage cityMessage = new CityMessage();
        cityMessage.setProvince("陕西");
        cityMessage.setCity("西安");
        cityMessage.setDistrict("长安");
        citiesUtil.setCityMessage(cityMessage);
        beginRefresh();
    }

    /**
     * 刷新数据
     */
    private void beginRefresh() {
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
        ScrollView myScrollView = (ScrollView) findViewById(R.id.myScrollView);
        myScrollView.smoothScrollTo(0, 0);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                beginRefresh();
            }
        });
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
        ImageView progressSpinner = (ImageView) splashDialog.findViewById(R.id.imgProgress);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.progress_animation);
        animation.setInterpolator(new LinearInterpolator());
        progressSpinner.startAnimation(animation);
    }

    /**
     * 关闭开头缓存/刷新页面
     */
    private void dismissSplashScreen() {
        if (splashDialog != null) {
            splashDialog.findViewById(R.id.imgProgress).clearAnimation();
            splashDialog.dismiss();
            splashDialog = null;
        }
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
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
                showSelectDialog();
                break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }

    /**
     * 开启一个显示选择城市的对话框，如果选择了城市信息则更新城市
     */
    private void showSelectDialog() {
        ArrayAdapter<String> selectAdapter = new ArrayAdapter<>(context, R.layout.select_city_item, R.id.txtCityItem, citiesUtil.getProvinces());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("选择城市");
        OnCitySelectListener onCitySelected = new OnCitySelectListener();
        onCitySelected.citiesUtil = citiesUtil;
        onCitySelected.selectAdapter = selectAdapter;

        builder.setSingleChoiceItems(selectAdapter, 0, onCitySelected);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (onCitySelected.isCompleted) {
                    citiesUtil.setCityMessage(onCitySelected.cityMessage);
                    beginRefresh();
                }
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 将name补位成两位数，如1补成01
     */
    private String nameConverter(String name) {
        if (name.length() == 1) return "0" + name;
        return name;
    }
}
