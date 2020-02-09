package com.avital.click4mom;

import java.util.Calendar;

public class Msg
{
    int num;
    Calendar calendar;
    String msg;
    String title;
    Boolean sent;



    public Msg (int num, String msg, String title)
    {
        this.title = title;
        this.num = num;
        this.msg = msg;
        sent = false;
        calendar = Calendar.getInstance ();
    }

    public void setCal (int day, int month, int year, int hour, int minute){
        calendar.set (Calendar.YEAR, year);
        calendar.set (Calendar.MONTH, month);
        calendar.set (Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
    }


    public String printMsg (){

        return msg + "\n\n";
    }

    public String getTitle (){

        return title;
    }


    public String printAllMsgDtl (){
        String str ="";

        str += "מספר הודעה: " + num + "\n";

        str += "תזמון: " + calendar.get (Calendar.DAY_OF_MONTH) + "-" + returnMonth (calendar.get (Calendar.MONTH)) + "-" + calendar.get (Calendar.YEAR)
                + " שעה: " + calendar.get (Calendar.HOUR_OF_DAY) + ":" + calendar.get (Calendar.MINUTE) + "\n";

        str += "הודעה: " + msg + "\n\n";

        return str;
    }

    public String returnMonth (int mm){
        switch (mm){
            case 0: return "JANUARY";
            case 1: return "FEBRUAR";
            case 2: return "MARCH";
            case 3: return "APRIL";
            case 4: return "MAY";
            case 5: return "JUNE";
            case 6: return "JULY";
            case 7: return "AUGOST";
            case 8: return "SEPTEMBER";
            case 9: return "OCTOBER";
            case 10: return "NOVEMBER";
            default: return "DECEMBER";
        }
    }

    public static String getDayByMsgNum (int num){

        num++;

        int res = num % 7;
        String day = "";


        if (num == 1 || res == 1)
            day = "הפרק השבועי";

        else if (num == 2 || res == 2)
            day = "יום שני - הודעת תזכורת";

        else if (num == 3 || res == 3)
            day = "יום שלישי - הודעת תזכורת";

        else if (num == 4 || res == 4)
            day = "יום רביעי - הודעת תזכורת";

        else if (num == 5 || res == 5)
            day = "יום חמישי - הודעת תזכורת";

        else if (num == 6 || res == 6)
            day = "יום שישי - הודעת תזכורת";

        else if (num == 7 || res == 7)
            day = "סיכום שבוע";


        return day;
    }
}

