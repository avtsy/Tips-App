package com.avital.click4mom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class updateSharedPreffernce extends AppCompatActivity {

    SharedPreferences sharedPref;


    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);

        // if you are here it means you were a free user and upgraded to payed

        sharedPref = getSharedPreferences ("SAVED_FILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putString ("REG", null);
        editor.commit ();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity (intent);
        finish ();
    }
}
