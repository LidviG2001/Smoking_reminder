package com.example.smokingreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String WHERE_MY_CAT_ACTION = "ru.alexanderklimov.action.CAT";
    Button setStartBtn, setStopBtn, startBtn, cancelBtn;
    EditText etInterval, etTimeFS;
    Calendar calendarStart, calendarStop;
    PendingIntent pendingIntent;
    TextView startTime_tv, stopTime_tv;
    SharedPreferences sPref;

    final String SAVED_START = "saved_start";
    final String SAVED_STOP = "saved_stop";
    final String SAVED_INTERVAL = "saved_interval";

    public static final String ACTION = "action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* getFragment();*/
    }


   /* private void getFragment() {
        if (Build.VERSION.SDK_INT >= 31){
            replaceFragment(new MainFragment());
        }
        else {
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    replaceFragment(new SplashFragment());
                }
            }, 2000);
            replaceFragment(new MainFragment());
        }
    }*/
    /*public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }*/
    /*public String convertTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(time);
    }
    void save(String key, long time) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(key, time);
        ed.apply();
    }

    public long extract(String key){
        sPref = getPreferences(MODE_PRIVATE);
        return sPref.getLong(key, 0l);
    }*/
}