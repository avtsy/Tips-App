package com.avital.click4mom;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;



public class UpdateToPayActivity extends Fragment {

    WebView webView;
    Animation fadeOut;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        final View rootView = inflater.inflate (R.layout.update_to_payed, container, false);

        webView = rootView.findViewById (R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://www.click4mom.com/reg");
        webView.setWebViewClient(new WebViewClient ());
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings ().setSupportZoom (false);
        webView.getSettings ().setUseWideViewPort (true);
        webView.getSettings ().setLoadWithOverviewMode (true);
        webView.getSettings ().getLoadsImagesAutomatically ();
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webView.setWebViewClient (new WebViewClient () {
                @Override
                public void onPageStarted (WebView view, String url, Bitmap favicon)
                {
                super.onPageStarted (view, url, favicon);

                fadeOut = new AlphaAnimation (1, 0);
                fadeOut.setInterpolator(new AccelerateInterpolator ());
                fadeOut.setStartOffset(0);
                fadeOut.setDuration(3000);

                ConstraintLayout cl = rootView.findViewById (R.id.cl);
                ImageView img = cl.findViewById (R.id.iv);
                img.startAnimation (fadeOut);
                cl.removeView (img);


            }

            @Override
            public void onPageFinished (WebView view, String url)
            {
                super.onPageFinished (view, url);

                fadeOut.cancel (); // if animation didnt gone until the page finsh uploaded, dismiss it
                fadeOut.reset ();
            }
        });


        return rootView;
    }


}