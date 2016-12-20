package com.dizsun.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.dizsun.weatherforecast.util.CitiesUtil;
import com.dizsun.weatherforecast.util.FileHelper;
import com.dizsun.weatherforecast.util.PureNetUtil;

public class WelcomeActivity extends AppCompatActivity {

    private WelcomeActivity context = this;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==PureNetUtil.WHAT_CITY){
                String value = ((String) msg.obj);
                try {
                    new CitiesUtil(context,value);
                } catch (Exception e) {
                    context.finish();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if(!FileHelper.existFile(this,FileHelper.CITIES_FILE)){
            new Thread(new PureNetUtil(PureNetUtil.GET_CITIES, null, handler)).start();
        }
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
