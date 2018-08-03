package de.a_b_software.anime_on_demand_kaze;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.webkit.WebView;

public class SeriesSite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        // Capture the layout's TextView and set the string as its text
        WebView wView = findViewById(R.id.webView);

        WebSettings webSettings = wView.getSettings();
        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setLoadWithOverviewMode(true);
        wView.getSettings().setUseWideViewPort(true);
        wView.getSettings().setBuiltInZoomControls(true);
        wView.getSettings().setDomStorageEnabled(true);
        wView.setWebViewClient(new MyWebViewClient());
        String position = getIntent().getStringExtra("EXTRA_POSITION");
        wView.loadUrl(getIntent().getStringExtra("EXTRA_LINK"));



    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            // do your handling codes here, which url is the requested url
            // probably you need to open that url rather than redirect:
            view.loadUrl(url);
            return false; // then it is not handled by default action
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        public void onPageFinished(WebView view, String url) {
            // do your stuff here
            view.loadUrl("javascript:document.getElementsByClassName('streamstarter_html5')[0].click()");
        }
    }

}
