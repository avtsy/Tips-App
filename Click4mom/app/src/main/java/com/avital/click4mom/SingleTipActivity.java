package com.avital.click4mom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class SingleTipActivity extends AppCompatActivity {

    ConstraintLayout cl;
    WebView webView;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_tips);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            StatusBarColor.setPinkColor (this);

        getWindow ().getDecorView ().setSystemUiVisibility (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // for black text on the white status bar

        Intent intent = getIntent ();
        int tipNum = intent.getExtras ().getInt ("TIP NUM", 0);
        String url = getUrl (tipNum);
        cl = findViewById (R.id.cl);



        webView = findViewById (R.id.webView);

        webView.loadUrl (url);
        WebSettings webSettings = webView.getSettings ();
        webSettings.setJavaScriptEnabled (true);
        webView.setWebViewClient (new WebViewClient ());
        webView.getSettings ().setRenderPriority (WebSettings.RenderPriority.HIGH);
        webView.getSettings ().setSupportZoom (false);
        webView.getSettings ().setUseWideViewPort (true);
        webView.getSettings ().setLoadWithOverviewMode (true);
        webView.getSettings ().getLoadsImagesAutomatically ();
        webView.getSettings ().setCacheMode (WebSettings.LOAD_NO_CACHE);
        webView.setLayerType (View.LAYER_TYPE_HARDWARE, null);
        webView.setLayerType (View.LAYER_TYPE_SOFTWARE, null);


        webView.setWebViewClient (new WebViewClient () {
            @Override
            public void onPageStarted (WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted (view, url, favicon);

                Animation fadeOut = new AlphaAnimation (1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator ());
                fadeOut.setStartOffset(0);
                fadeOut.setDuration(800);

                ImageView img = cl.findViewById (R.id.iv);
                img.startAnimation (fadeOut);
                cl.removeView (img);
            }
        });

    }


    private String getUrl (int tipNum) {

        String url = "https://www.click4mom.com";

        return url;
    }

    @Override
    protected void onStop ()
    {
        super.onStop ();
        finish ();
    }
}
