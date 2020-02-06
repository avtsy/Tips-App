package com.avital.click4mom;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.ALARM_SERVICE;

public class MyReceiver extends BroadcastReceiver {

    final String CHANNEL_ID = "777";


    @Override
    public void onReceive (Context context, Intent intent)
    {
        intent.setClass (context, AlertDtl.class);
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity (context, 0, intent, 0);


        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder (context, CHANNEL_ID)
                .setSmallIcon (R.drawable.single_pics_logo)
                .setContentTitle (intent.getStringExtra ("TITLE"))
                .setContentText (intent.getStringExtra ("CONTENT"))
                .setStyle (new NotificationCompat.BigTextStyle ()
                        .bigText (intent.getStringExtra ("CONTENT"))) // change for longest content
                .setPriority (NotificationCompat.PRIORITY_HIGH)
                .setContentIntent (pendingIntent)
                .setAutoCancel (true)
                .setBadgeIconType (NotificationCompat.BADGE_ICON_SMALL)
                .setVisibility (NotificationCompat.VISIBILITY_PUBLIC); // for lock screen

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from (context);
        notificationManager.notify (intent.getIntExtra ("ID", 0), builder.build ());
    }


    public void setAllNotifications (Context context, Msg msg) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService (ALARM_SERVICE);

        Intent intent = new Intent (context, MyReceiver.class);
        intent.putExtra ("TITLE", msg.title);
        intent.putExtra ("CONTENT", msg.msg);
        intent.putExtra ("ID", msg.num); // doesnt matter/ the id at the PendingIntent is the important

        PendingIntent alarmIntent = PendingIntent.getBroadcast (context, msg.num, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance ();
        calendar.setTimeInMillis (System.currentTimeMillis ());
        calendar.set (Calendar.YEAR, msg.calendar.get (Calendar.YEAR));
        calendar.set (Calendar.MONTH, msg.calendar.get (Calendar.MONTH));
        calendar.set (Calendar.DAY_OF_MONTH, msg.calendar.get (Calendar.DAY_OF_MONTH));
        calendar.set (Calendar.HOUR_OF_DAY, msg.calendar.get (Calendar.HOUR_OF_DAY));
        calendar.set (Calendar.MINUTE, msg.calendar.get (Calendar.MINUTE));
        calendar.set (Calendar.SECOND, msg.calendar.get (Calendar.SECOND));
        calendar.add (Calendar.SECOND, 3);
        alarmManager.set (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (), alarmIntent);
    }
}
