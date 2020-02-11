package com.avital.click4mom;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Tag";
    StringRequest stringRequest;
    RequestQueue queue;
    SharedPreferences sharedPref;
    Mom mom;
    int tryings = 0;
    String user, phone = "0";

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            StatusBarColor.setPinkColor (this);

        getWindow ().getDecorView ().setSystemUiVisibility (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // for black text on the white status bar


        queue = Volley.newRequestQueue (this);

        // CHECKS FIRST TIME OR OTHERS
        sharedPref = getSharedPreferences ("SAVED_FILE", Context.MODE_PRIVATE);
        user = sharedPref.getString ("REG", "null");


        switch (user) {

            case "null": // first time EVER

                        user = "free reg";
                        SharedPreferences.Editor editor = sharedPref.edit ();
                        editor.putString ("REG", "free reg");
                        editor.commit ();

                        getDataFromServer (); // get the datalist of free tips

                        break;

            case "free reg": // free but not first time

                        Intent intent = new Intent (this, MainFreeUserActivity.class);
                        startActivity (intent);
                        finish ();
                        break;

            case "payed reg first": // payed, first time

                        phone = sharedPref.getString ("PHONE", null);
                        getDataFromServer ();
                        break;

            default: // payed and it is not his first time

                        intent = new Intent (this, MainPayedUserActivity.class);
                        startActivity (intent);
                        finish ();
        }
    }


    public void getDataFromServer ()
    {
        String url;

        if (user.equals ("free reg"))
            url = "https://avitalh0.wixsite.com/mysite/_functions/freeData";
        else
            url = "https://avitalh0.wixsite.com/mysite/_functions/payedUser" + phone;


        stringRequest = new StringRequest (Request.Method.GET, url,
                new Response.Listener<String> () {
                    @Override
                    public void onResponse (String response) {

                        updateInternalData (response);
                    }
                }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (tryings < 5) {

                    tryings++;
                    getDataFromServer ();
                } else
                    Toast.makeText (getApplicationContext (), "משהו השתבש", Toast.LENGTH_LONG).show ();
            }
        });

        stringRequest.setTag (TAG);
        queue.add (stringRequest);
    }


    public void updateInternalData (String data){ // update the internal store with all user data

        try
        {
            JSONObject obj = new JSONObject (data); // Json for all the data from wix
            JSONArray JmsgArr = obj.getJSONArray ("DATA");

            Msg msgArr[] = new Msg[JmsgArr.length ()];
            Msg m;
            int msgNum = obj.getInt ("TOTAL MSG NUM");

            // make the messages array
            for (int i = 0; i < JmsgArr.length (); i++) {

                JSONObject one = JmsgArr.getJSONObject (i);
                m = new Msg (one.getInt ("MSG_NUM"), one.getString ("MSG"), one.getString ("TITLE"));
                msgArr[i] = m;
            }

            if (user.equals ("free reg"))
                mom = new Mom (msgNum, msgArr);

            else // payed
                mom = new Mom (obj.getString ("NAME"), obj.getString ("PHONE"), obj.getInt ("WEEKS NUMBER"), msgNum, msgArr, obj.getInt ("MSG HOUR"), obj.getString ("APP REG DATE"));

            mom.atMsgNum = 1;

            Gson gson = new Gson ();

            String dataToSave = gson.toJson (mom);

            SharedPreferences sharedPref = getSharedPreferences ("SAVED_FILE", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit ();
            editor.putString ("MOM_OBJ", dataToSave);

            editor.commit ();

            Intent intent;

            if (user.equals ("free reg"))
                intent = new Intent (this, MainFreeUserActivity.class);

            else
                intent = new Intent (this, MainPayedUserActivity.class);

            startActivity (intent);
            finish ();

        } catch (JSONException e)
        {
            e.printStackTrace ();

            SharedPreferences.Editor editor = sharedPref.edit ();
            editor.putString ("REG", null);
            editor.commit ();
        }
    }


    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
}
