package com.avital.click4mom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Tips extends Fragment {

    String[] tipsArr;
    ListView listView;
    ArrayAdapter arrAdp;
    SharedPreferences sharedPref;
    int code;

    public Tips (){}

    public Tips (String[] tipsArr, SharedPreferences sharedPref) {

        this.tipsArr = tipsArr;
        this.sharedPref = sharedPref;
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.tips, container, false);
        listView = rootView.findViewById(R.id.list_view);

        arrAdp = new ArrayAdapter<> (getContext (), android.R.layout.simple_list_item_1, tipsArr);

        listView.setAdapter(arrAdp);

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent (getContext (), SingleTipActivity.class);
                intent.putExtra ("TIP NUM", position + 1);
                startActivity (intent);
            }
        });

        code = sharedPref.getInt ("CODE", 0);

        if (code != -1)
            makeShareView (rootView);

        return rootView;
    }

    private void makeShareView (View v){

        View vv = v.findViewById (R.id.list_view);
        LinearLayout.LayoutParams loparams = (LinearLayout.LayoutParams) vv.getLayoutParams();
        loparams.weight = 1;

        vv = v.findViewById (R.id.lini);
        loparams = (LinearLayout.LayoutParams) vv.getLayoutParams();
        loparams.weight = 2;

        ImageView iv = v.findViewById (R.id.iv);

        iv.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v)
            {

                if (code == 0) { // make A a code, or use her previous code

                    Random rnd = new Random ();
                    code = rnd.nextInt (1000000);

                    SharedPreferences.Editor editor = sharedPref.edit ();
                    editor.putInt ("CODE", code);
                    editor.commit ();
                }

                String msg = "היי! ממליצה לך להוריד את אפליקציית הטיפים של *אמא בקליק*, היא ממש כיפית!";
                msg += "\nלפרטים:";
                msg += " ";

                msg += "https://avitalh0.wixsite.com/mysite/sharingpage?sc=" + code;



                Intent sendIntent = new Intent ();
                sendIntent.setAction (Intent.ACTION_SEND);
                sendIntent.putExtra (Intent.EXTRA_TEXT, msg);
                sendIntent.setType ("text/plain");
                sendIntent.setPackage ("com.whatsapp");

                // close app
                if(Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 21){
                    getActivity ().finishAffinity ();
                } else if (Build.VERSION.SDK_INT>=21){
                    getActivity ().finishAndRemoveTask ();
                }

                startActivity (sendIntent);

            }
        });
    }
}