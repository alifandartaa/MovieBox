package com.example.secondappcataloguemovie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.secondappcataloguemovie.AlarmReminder;
import com.example.secondappcataloguemovie.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    final boolean DEFAULT_DAILY = false;
    final boolean DEFAULT_RELEASE = false;
    private final String KEY_DAILY = "daily";
    private final String KEY_RELEASE = "release";
    private Switch dailySwitch;
    private Switch releaseSwitch;
    private final String SHARED_PREF = "Settings";
    private AlarmReminder alarmReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        alarmReminder = new AlarmReminder();
        dailySwitch = findViewById(R.id.sw_daily);
        releaseSwitch = findViewById(R.id.sw_release);


        SharedPreferences mPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);

        boolean isDailyReminderOn = mPreferences.getBoolean(KEY_DAILY, DEFAULT_DAILY);
        boolean isReleaseReminderOn = mPreferences.getBoolean(KEY_RELEASE, DEFAULT_RELEASE);
        Log.d("Setting", "Preference: " + isReleaseReminderOn + " " + isDailyReminderOn);

        dailySwitch.setChecked(isDailyReminderOn);
        releaseSwitch.setChecked(isReleaseReminderOn);
        releaseSwitch.setOnClickListener(this);
        dailySwitch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sw_daily:
                SharedPreferences.Editor dailyPref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
                dailyPref.putBoolean(KEY_DAILY, dailySwitch.isChecked());
                dailyPref.apply();
                dailySwitch.setChecked(dailySwitch.isChecked());
                alarmReminder.setDailyReminder(getApplicationContext(), AlarmReminder.TYPE_DAILY, dailySwitch.isChecked());
                break;
            case R.id.sw_release:
                SharedPreferences.Editor releasePref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE).edit();
                releasePref.putBoolean(KEY_RELEASE, releaseSwitch.isChecked());
                releasePref.apply();
                releaseSwitch.setChecked(releaseSwitch.isChecked());
                alarmReminder.setReleaseReminder(getApplicationContext(), AlarmReminder.TYPE_RELEASE, releaseSwitch.isChecked());
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
