package com.avital.click4mom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

public class AlertDtl extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_alert_dtl);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            StatusBarColor.setPinkColor (this);

        getWindow ().getDecorView ().setSystemUiVisibility (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // for black text on the white status bar


        TextView tv = findViewById (R.id.tip_text);

        Intent intent = getIntent ();

        SharedPreferences sharedPref = getSharedPreferences ("SAVED_FILE", Context.MODE_PRIVATE);
        String json = sharedPref.getString ("MOM_OBJ", "");
        Gson gson = new Gson ();
        Mom mom = gson.fromJson (json, Mom.class);

        int tip = intent.getIntExtra ("ID", -1);

        tv.setText (mom.msg [tip - 1].msg);
    }

    @Override
    protected void onStop ()
    {
        super.onStop ();
        finish ();
    }
}