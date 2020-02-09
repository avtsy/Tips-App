package com.avital.click4mom;

import android.content.Context;
import java.util.Calendar;


public class Mom {

    String name;
    String phone;
    int weekNum;
    int totalMsgNum;
    Msg msg[];
    int msgHour; // 11 or 17
    String AppRegDate;
    int atMsgNum;


    // payed mom
    public Mom (String name, String phone, int weekNum, int totalMsgNum, Msg [] msgArr, int hour, String AppRegDate)
    {
        this.name = name;
        this.phone = phone;
        this.weekNum = weekNum;
        this.totalMsgNum = totalMsgNum;
        this.msgHour = hour;
        this.AppRegDate = AppRegDate;
        msg = msgArr; //******** + more messages beside the weekly alerts?
        atMsgNum = 0;
    }


    // free mom
    public Mom (int totalMsgNum, Msg [] msgArr){

        name = "free user";
        this.totalMsgNum = totalMsgNum;
        msgHour = 17;
        msg = msgArr;
        weekNum = 1000000;
        atMsgNum = 1;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int mm = calendar.get (Calendar.MONTH);
        int yy = calendar.get (Calendar.YEAR);

        AppRegDate = dd + "/" + mm + "/" + yy ;

    }


    public void msgAddTimes (Context context){ // add schedules to msg array


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        if (weekNum == 1000000) {// free user

            makeTipsTimes (calendar); // the calendar is today

        } else {

            String[] date = AppRegDate.split ("/");

            //set calendar to the app reg date
            calendar.set (Integer.parseInt (date[2]), Integer.parseInt (date[1]) - 1, Integer.parseInt (date[0]));

            makePayedTimes (calendar);
        }
    }


    public void makeTipsTimes (Calendar calendar){

        msg [0].setCal (0, 0, 0, 0, 0); // first tip already done

        //calendar.add (Calendar.MINUTE, 2);
        calendar.add (Calendar.SECOND, 10);

        for (int i = 1; i < totalMsgNum; i++){ // skip first tip

            msg [i].setCal (calendar.get (Calendar.DAY_OF_MONTH), calendar.get (Calendar.MONTH), calendar.get (Calendar.YEAR), calendar.get (Calendar.HOUR_OF_DAY), calendar.get (Calendar.MINUTE));

            calendar.add (Calendar.HOUR_OF_DAY, 72);

            // skip Shabat
            if (calendar.get(Calendar.DAY_OF_WEEK) == 7)
                calendar.add (Calendar.HOUR_OF_DAY, 24);

            calendar.set (Calendar.HOUR_OF_DAY, 20);
            calendar.set (Calendar.MINUTE, 30);
        }

    }



    public void makePayedTimes (Calendar calendar){

        int day = calendar.get(Calendar.DAY_OF_WEEK); // day == today

        if (day != 1) // move calendar to sunday
        {
            day = 8 - day;
            calendar.add (Calendar.HOUR_OF_DAY, day * 24);
        }

        int msgNum = 1;

        for (int week = 1; week <= weekNum; week++){ // 7 or 8 weeks loop

            //set first msg of the week

            msg [msgNum - 1].setCal (calendar.get (Calendar.DAY_OF_MONTH), calendar.get (Calendar.MONTH), calendar.get (Calendar.YEAR), 9, 0);

            calendar.add (Calendar.HOUR_OF_DAY, 24);
            msgNum++;

            // 6 days msgs
            for (int d = 0; d < 5; d ++){
                msg [msgNum - 1].setCal (calendar.get (Calendar.DAY_OF_MONTH), calendar.get (Calendar.MONTH), calendar.get (Calendar.YEAR), msgHour, 0);

                calendar.add (Calendar.HOUR_OF_DAY, 24);
                msgNum++;
            }

            //set last msg of the week
            msg [msgNum - 1].setCal (calendar.get (Calendar.DAY_OF_MONTH), calendar.get (Calendar.MONTH), calendar.get (Calendar.YEAR), 20, 30);

            calendar.add (Calendar.HOUR_OF_DAY, 24);
            msgNum++;
        }

    }
}

