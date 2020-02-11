package com.avital.click4mom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;


public class MainPayedUserActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navView;
    TextView content;
    TextView title;
    Mom mom;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main_payed_user);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            StatusBarColor.setPinkColor (this);

        getWindow ().getDecorView ().setSystemUiVisibility (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // for black text on the white status bar

        drawerLayout = findViewById (R.id.drawer_layout);
        navView = findViewById (R.id.navigation_view);
        content = findViewById (R.id.hi);
        title = findViewById (R.id.week_title);

        SharedPreferences sharedPreferences = getSharedPreferences ("SAVED_FILE", Context.MODE_PRIVATE);

        String json = sharedPreferences.getString ("MOM_OBJ", "");
        Gson gson = new Gson ();
        mom = gson.fromJson (json, Mom.class);

        makeDrawer (mom.atMsgNum, mom.name);



        navView.setNavigationItemSelectedListener (new NavigationView.OnNavigationItemSelectedListener () {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem menuItem)
            {
                drawerLayout.closeDrawers ();

                ScrollView sv = findViewById(R.id.scrolly);
                sv.smoothScrollTo (0,0);

                title.setText (Html.fromHtml ("<b>" + menuItem.toString () + "</b>"));
                title.append ("\n\n");

                content.setText ("");


                int week = getWeekNum (menuItem);

                week--;

                for (int i = week * 7, j = 0; i < mom.atMsgNum && j < 7; i++, j++) {


                    content.append (Html.fromHtml ("<b>" + Msg.getDayByMsgNum (i) + "</b>"));

                    content.append ("\n\n" + mom.msg [i].printMsg () + "\n");

                }

                sv.smoothScrollTo (0,0);

                return false;
            }
        });
    }


    private int getWeekNum (MenuItem item) {

        int week = 0;

        switch (item.getItemId ()){

            case R.id.week1: week = 1; break;
            case R.id.week2: week = 2; break;
            case R.id.week3: week = 3; break;
            case R.id.week4: week = 4; break;
            case R.id.week5: week = 5; break;
            case R.id.week6: week = 6; break;
            case R.id.week7: week = 7; break;
            case R.id.week8: week = 8; break;
        }

        return week;
    }



    private void makeDrawer (int msgNum, String name){

        TextView navHeader = navView.getHeaderView (0).findViewById (R.id.hi);
        navHeader.setText (getString(R.string.hi) + " " + name);

        for (int i = 0, j = 1; i < 8; i++, j+=7){

            if (msgNum >= j) {

                MenuItem item = navView.getMenu ().getItem (i);
                item.setEnabled (true);
                SpannableString spanString = new SpannableString (item.getTitle ().toString ());
                spanString.setSpan (new ForegroundColorSpan (ContextCompat.getColor (this, R.color.colorPrimary)), 0, spanString.length (), 0);
                item.setTitle (spanString);
            }
        }
    }

}
