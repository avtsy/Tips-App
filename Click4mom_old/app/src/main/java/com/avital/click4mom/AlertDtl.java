package com.avital.click4mom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AlertDtl extends AppCompatActivity
{

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_alert_dtl);

        TextView tv = findViewById (R.id.tv);

        Intent in = getIntent ();

        tv.setText (in.getStringExtra ("MSG"));
    }
}