package com.example.smokingreminder;

import static android.content.Context.MODE_PRIVATE;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.window.SplashScreen;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etTimeFS = (EditText) view.findViewById(R.id.etTimeFS);

        etInterval = (EditText) view.findViewById(R.id.etInterval);

        startTime_tv = (TextView) view.findViewById(R.id.startTime_tv);

        stopTime_tv = (TextView) view.findViewById(R.id.stopTime_tv);

        setStartBtn = (Button) view.findViewById(R.id.setStartBtn);

        setStopBtn = (Button) view.findViewById(R.id.setStopBtn);

        startBtn = (Button) view.findViewById(R.id.startBtn);

        cancelBtn = (Button) view.findViewById(R.id.cancelBtn);

        startTime_tv.setText( "Start time: " + convertTime(extract(SAVED_START), "HH:mm"));

        stopTime_tv.setText( "Stop time: " + convertTime(extract(SAVED_STOP), "HH:mm"));

        etInterval.setText(String.valueOf(extract(SAVED_INTERVAL)));

        /*setStartBtn.setOnClickListener(view ->{
            ((MainActivity) getActivity()).startTime();
        });*/

        setStartBtn.setOnClickListener( a -> {
            Log.d("LOG", "1234");
            MaterialTimePicker materialTimePickerStart = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Test1")
                    .build();
            materialTimePickerStart.addOnPositiveButtonClickListener(v -> {
                calendarStart = Calendar.getInstance();
                calendarStart.set(Calendar.MINUTE, materialTimePickerStart.getMinute());
                calendarStart.set(Calendar.HOUR_OF_DAY, materialTimePickerStart.getHour());

                // SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                startTime_tv.setText("Start time: " + convertTime(calendarStart.getTimeInMillis(), "HH:mm"));

                save(SAVED_START, calendarStart.getTimeInMillis());

            });
            Log.d("LOG", "before click");
            materialTimePickerStart.show(getChildFragmentManager(), "tag_picker");
            Log.d("LOG", "after click");
        });

        setStopBtn.setOnClickListener( b -> {
            Log.d("LOG", "5678");
            MaterialTimePicker materialTimePickerStop = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Test1")
                    .build();
            materialTimePickerStop.addOnPositiveButtonClickListener(w -> {
                calendarStop = Calendar.getInstance();
                calendarStop.set(Calendar.MINUTE, materialTimePickerStop.getMinute());
                calendarStop.set(Calendar.HOUR_OF_DAY, materialTimePickerStop.getHour());

                stopTime_tv.setText("Stop time: " + convertTime(calendarStop.getTimeInMillis(), "HH:mm"));

                save(SAVED_STOP, calendarStop.getTimeInMillis());

            });
            materialTimePickerStop.show(getActivity().getSupportFragmentManager(), "tag_picker");
        });

        startBtn.setOnClickListener( c -> {
            Log.d("LOG", "4321");
            if (etInterval.getText().toString().length() == 0) return;
            Log.d("TAG", "До перевода в инт" + etInterval.getText().toString());
            int value = Integer.parseInt(etInterval.getText().toString());
            Log.d("TAG", "После перевода в инт" + value);

            if (value <= 0) {
                value = 1;
                return;
            }

            Log.d("TAG", "Проверка");

            int interval = value;

            save(SAVED_INTERVAL, interval);

            Intent intent = new Intent(getActivity(), NotficationListener.class);
            intent.putExtra("ru.alexanderklimov.broadcast.Message", etTimeFS.getText().toString());
            pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendarStart.getTimeInMillis(),1000 * 60 * interval, pendingIntent);

            Intent cancellationIntent = new Intent(getActivity(), CancelAlarmBroadcastReceiver.class);
            cancellationIntent.putExtra("key", pendingIntent);
            PendingIntent cancellationPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, cancellationIntent, PendingIntent.FLAG_IMMUTABLE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, calendarStop.getTimeInMillis(), cancellationPendingIntent);
        });

        cancelBtn.setOnClickListener( d -> {
            Intent cancellationIntent = new Intent(getActivity(), CancelAlarmBroadcastReceiver.class);
            cancellationIntent.putExtra("key", pendingIntent);
            PendingIntent cancellationPendingIntent = PendingIntent.getBroadcast(getActivity(), 0, cancellationIntent, PendingIntent.FLAG_IMMUTABLE);

            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, 0, cancellationPendingIntent);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_main, container, false);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public String convertTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(time);
    }
    void save(String key, long time) {
        sPref = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(key, time);
        ed.apply();
    }

    public long extract(String key){
        sPref = getActivity().getPreferences(MODE_PRIVATE);
        return sPref.getLong(key, 0l);
    }
}