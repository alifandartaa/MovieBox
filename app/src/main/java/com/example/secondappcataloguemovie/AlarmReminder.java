package com.example.secondappcataloguemovie;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.secondappcataloguemovie.activities.MainActivity;
import com.example.secondappcataloguemovie.api.MovieViewModel;
import com.example.secondappcataloguemovie.model.Movie;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReminder extends BroadcastReceiver {

    public static final String TYPE_RELEASE = "ReleaseReminder";
    public static final String TYPE_DAILY = "DailyReminder";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    private static final int ID_DAILY = 101;
    private static final int ID_RELEASE = 100;
    private static final int ID_OPEN_APP = 102;

    private ArrayList<Movie> listMovies = new ArrayList<>();
    private int notificationID;

    public AlarmReminder() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        if (Objects.requireNonNull(type).equalsIgnoreCase(TYPE_DAILY)) {
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            String title = context.getResources().getString(R.string.app_name);
            notificationID = ID_DAILY;
            setupAlarmNotification(context, title, message, notificationID);
        } else {
            notificationID = ID_RELEASE;
            new MovieViewModel().getReleaseMovies(new MovieViewModel.ReleaseMovieListener() {
                @Override
                public void onSuccess(ArrayList<Movie> movies) {
                    listMovies = movies;
                    for (Movie mv : listMovies) {
                        Log.d("Alarm", "list movie: " + mv.getTitle());
                        setupAlarmNotification(context, mv.getTitle(), mv.getTitle() + context.getResources().getString(R.string.release_message), notificationID++);
                    }
                }

                @Override
                public void onError(boolean isFailed) {
                    if (isFailed) {
                        Toast.makeText(context, "Failed load movie notification", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setupAlarmNotification(Context context, String title, String message, int notificationID) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        Uri alarmSound = RingtoneManager.getDefaultUri(TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_NAME)
                .setSmallIcon(R.drawable.ic_access_alarm_black)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent));
//                .setSound(alarmSound);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, ID_OPEN_APP, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);
            if (notificationManagerCompat != null) {
                notificationManagerCompat.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        if (notificationManagerCompat != null) {
            notificationManagerCompat.notify(notificationID, notification);
        }
    }


    public void setDailyReminder(Context context, String type, boolean isReminderOn) {
        if (!isReminderOn) {
            cancelAlarm(context, type);
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReminder.class);
        intent.putExtra(EXTRA_MESSAGE, context.getResources().getString(R.string.content_text));
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, context.getResources().getString(R.string.dailyreminder_on), Toast.LENGTH_SHORT).show();
    }

    public void setReleaseReminder(Context context, String type, boolean isReminderOn) {
        if (!isReminderOn) {
            cancelAlarm(context, type);
            return;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReminder.class);
        intent.putExtra(EXTRA_TYPE, type);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Toast.makeText(context, context.getResources().getString(R.string.releasereminder_on), Toast.LENGTH_SHORT).show();
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReminder.class);

        int requestCode = type.equalsIgnoreCase(TYPE_RELEASE) ? ID_RELEASE : ID_DAILY;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }

        boolean isDailyReminder = type.equalsIgnoreCase(TYPE_DAILY);
        Toast.makeText(context, context.getResources().getString(isDailyReminder ? R.string.dailyreminder_off : R.string.releasereminder_off), Toast.LENGTH_LONG).show();
    }
}
