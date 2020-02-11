package com.avital.click4mom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class MainFreeUserActivity extends AppCompatActivity {

    public static final String TAG_CODE = "secretCode";
    private static final String CHANNEL_ID = "777";

    StringRequest stringRequestCode;
    RequestQueue queueCode;
    int tryings = 0;
    SharedPreferences sharedPref;
    ViewPager viewPager;
    Mom mom;
    int code;
    Context context;
    int max;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main_free_user);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            StatusBarColor.setPinkColor (this);

        getWindow ().getDecorView ().setSystemUiVisibility (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // for black text on the white status bar


        sharedPref = getSharedPreferences ("SAVED_FILE", Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume () {

        super.onResume ();

        // get saved mom
        String json = sharedPref.getString ("MOM_OBJ", "");
        Gson gson = new Gson ();
        mom = gson.fromJson (json, Mom.class);

        // update current notification
        int current = sharedPref.getInt ("CURRENT", -1);

        if (current != -1) // -1 means that mom haven't get notifi yet
            mom.atMsgNum = current;

        if (max != 0 && max != mom.atMsgNum) { // user has app open so we need to refresh ui
            startActivity (Intent.makeRestartActivityTask (this.getIntent ().getComponent ()));

        } else
            setUi ();
    }

    @Override
    protected void onPause () {

        super.onPause ();

        // get current from notifications reciever
        mom.atMsgNum = sharedPref.getInt ("CURRENT", 1);

        // save mom for next time
        Gson gson = new Gson ();
        String momToSave = gson.toJson (mom);
        SharedPreferences.Editor editor = sharedPref.edit ();
        editor.putString ("MOM_OBJ", momToSave);
        editor.commit ();

        if (max != mom.atMsgNum && max != 0) { // need to update the ui next time. remove app from background

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                super.finishAndRemoveTask ();

            else
                super.finish ();
        }
    }

    public void setUi () {
        code = sharedPref.getInt ("CODE", 0);

        if (code == -1) { // shared

            makeTabs (); // make the tabs layout etc

        } else if (code == 0) { // never shared

            makeTabs ();

        } else  // A code exist but still don't know if shared or not

            checkCode ();
    }


    private void makeTabs () {
        viewPager = findViewById (R.id.tabViewPager);
        TabLayout tabLayout = findViewById (R.id.tabLayout);

        setupViewPager (viewPager);

        tabLayout.setupWithViewPager (viewPager);

        tabLayout.addOnTabSelectedListener (new TabLayout.OnTabSelectedListener () {
            @Override
            public void onTabSelected (TabLayout.Tab tab) {

                viewPager.setCurrentItem (tab.getPosition ());
            }

            @Override
            public void onTabUnselected (TabLayout.Tab tab) { }

            @Override
            public void onTabReselected (TabLayout.Tab tab) { }
        });
    }


    private void setupViewPager (ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter (getSupportFragmentManager ());

        adapter.addFrag (new Tips (showTipsList (), sharedPref), "טיפים");
        adapter.addFrag (new UpdateProfileActivity (), "פרופיל");
        adapter.addFrag (new UpdateToPayActivity (), "שדרוג");

        viewPager.setAdapter (adapter);
    }


    private String[] showTipsList () {
        max = mom.atMsgNum;

        String[] str = new String[max];

        for (int i = 0; i < max; i++)
            str[i] = mom.msg[i].getTitle ();

        return str;
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<> ();
        private final List<String> mFragmentTitleList = new ArrayList<> ();

        public ViewPagerAdapter (FragmentManager manager)
        {
            super (manager);
        }

        @Override
        public Fragment getItem (int position) {
            return mFragmentList.get (position);
        }

        @Override
        public int getCount () {
            return mFragmentList.size ();
        }

        public void addFrag (Fragment fragment, String title) {
            mFragmentList.add (fragment);
            mFragmentTitleList.add (title);
        }

        @Override
        public CharSequence getPageTitle (int position) {
            return mFragmentTitleList.get (position);
        }
    }


    private void checkCode () {
        context = this;

        queueCode = Volley.newRequestQueue (getApplicationContext ());

        String url = "https://avitalh0.wixsite.com/mysite/_functions/checkCode/" + code;

        stringRequestCode = new StringRequest (Request.Method.GET, url,
                new Response.Listener<String> () {
                    @Override
                    public void onResponse (String response)
                    {

                        if (response.contains ("1")) { // B downloaded
                            // updat code
                            sharedPref = getSharedPreferences ("SAVED_FILE", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit ();
                            editor.putInt ("CODE", -1);
                            editor.commit ();
                            code = -1;

                            // update tips time
                            String json = sharedPref.getString ("MOM_OBJ", "");
                            Gson gson = new Gson ();
                            mom = gson.fromJson (json, Mom.class);

                            // create notifications
                            createNotificationChannel ();
                            mom.msgAddTimes ();
                            addAlerts (mom);

                            // save mom at internal store
                            gson = new Gson ();
                            String momToSave = gson.toJson (mom);
                            editor.putString ("MOM_OBJ", momToSave);
                            editor.commit ();

                            makeTabs (); // make the tabs layout etc

                        } else { // A still didn't share or B didn't download

                            makeTabs (); // make the tabs layout etc
                        }
                    }
                }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse (VolleyError error) {
                if (tryings < 5) {
                    tryings++;
                    checkCode ();
                } else
                    Toast.makeText (getApplicationContext (), "תקלה בחיבור", Toast.LENGTH_SHORT).show ();
            }
        });

        queueCode.add (stringRequestCode);
        stringRequestCode.setTag (TAG_CODE);

    }

    private void addAlerts (Mom mom) {
        MyReceiver rc = new MyReceiver ();

        for (int i = 1; i < mom.totalMsgNum; i++)
            rc.setSingleNotifications (this, mom.msg[i]);
    }

    private void addSingleAlert (Msg msg) {
        createNotificationChannel ();
        MyReceiver rc = new MyReceiver ();

        rc.setSingleNotifications (this, msg);
    }


    private void createNotificationChannel () {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "click4mom";
            String description = "daily alert";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel (CHANNEL_ID, name, importance);
            channel.setDescription (description);

            NotificationManager notificationManager = getSystemService (NotificationManager.class);
            notificationManager.createNotificationChannel (channel);
        }
    }


    @Override
    protected void onStop ()
    {
        super.onStop ();
        if (queueCode != null)
            queueCode.cancelAll (TAG_CODE);

    }
}
