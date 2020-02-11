package com.avital.click4mom;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;

import java.util.Calendar;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.ALARM_SERVICE;

public class MyReceiver extends BroadcastReceiver {

    final String CHANNEL_ID = "777";
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @RequiresApi (api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive (Context context, Intent intent)
    {
        // get the relevant content
        pref = context.getSharedPreferences ("SAVED_FILE", Context.MODE_PRIVATE);

        int atMsgNum = pref.getInt ("CURRENT", 1);

        String json = pref.getString ("MOM_OBJ", "");
        Gson gson = new Gson ();
        Mom mom = gson.fromJson (json, Mom.class);

        String titleStr = mom.msg [atMsgNum].title;
        String contentStr = mom.msg [atMsgNum].msg;

        atMsgNum++;


        // update current tip num
        editor =  pref.edit ();
        editor.putInt ("CURRENT", atMsgNum);
        editor.commit ();


        // intent for showing the tip activity
        intent.setClass (context, OnPressTipNotificationActivity.class);
        intent.putExtra ("ID", atMsgNum);
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        // notification
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent =  stackBuilder.getPendingIntent(atMsgNum, 0);

        NotificationCompat.Builder builder;

        builder = new NotificationCompat.Builder (context, CHANNEL_ID)
                .setSmallIcon (R.drawable.single_pics_logo)
                .setContentTitle (titleStr)
                .setContentText (contentStr)
                .setStyle (new NotificationCompat.BigTextStyle ()
                        .bigText (contentStr))
                .setPriority (NotificationCompat.PRIORITY_HIGH)
                .setContentIntent (pendingIntent)
                .setAutoCancel (true)
                .setBadgeIconType (NotificationCompat.BADGE_ICON_SMALL)
                .setVisibility (NotificationCompat.VISIBILITY_PUBLIC); // for lock screen

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from (context);
        notificationManager.notify (atMsgNum, builder.build ());
    }


    public void setSingleNotifications (Context context, Msg msg) {


        Intent intent = new Intent (context, MyReceiver.class);

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

        AlarmManager alarmManager = (AlarmManager) context.getSystemService (ALARM_SERVICE);
        alarmManager.set (AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis (), alarmIntent);
    }
}
