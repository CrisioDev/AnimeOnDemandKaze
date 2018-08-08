package de.a_b_software.anime_on_demand_kaze;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebViewClient;
import android.webkit.WebView;

public class SeriesSite extends AppCompatActivity {

    String position = "";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        try{
            getSupportActionBar().hide(); //hide the title bar
        }catch(NullPointerException exception){
            exception.printStackTrace();
        }


        setContentView(R.layout.webview);

        // Hide the Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Get the Intent that started this activity and extract the string
        //Intent intent = getIntent();

        // Capture the layout's TextView and set the string as its text
        WebView wView = findViewById(R.id.webView);

        //WebSettings webSettings = wView.getSettings();
        wView.getSettings().setJavaScriptEnabled(true);
        wView.getSettings().setLoadWithOverviewMode(true);
        wView.getSettings().setUseWideViewPort(true);
        wView.getSettings().setDomStorageEnabled(true);
        wView.setWebViewClient(new MyWebViewClient());
        position = getIntent().getStringExtra("EXTRA_POSITION");
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
            view.loadUrl("javascript:document.getElementsByClassName('episodebox')["+position+"].getElementsByClassName('streamstarter_html5')[0].click()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('l-contentcontainer')[1].getElementsByClassName('three-box-container')[0].style.display = 'none'; " +
                    "})()");
            view.loadUrl("javascript:(function() { " +
                    "var children = document.getElementsByClassName('l-contentcontainer')[1].getElementsByTagName('*')[0].childNodes;\n" +
                    "     for(var c=0; c < children.length; c++) {\n" +
                    "      if(children[c].style) {\n" +
                    "       children[c].style.display = 'none';\n" +
                    "      }\n" +
                    "     }" +
                    "document.getElementById(\"player_container\").style.display = \"block\"; \n" +
                    "document.getElementsByClassName('l-header')[0].style.display = 'none'; \n" +
                    "document.getElementsByClassName('l-contentcontainer-footer')[0].style.display = 'none'; \n" +
                    "document.getElementsByClassName('l-contentcontainer')[1].getElementsByTagName('h2')[0].style.display = 'none'; \n" +
                    "document.getElementsByClassName('l-contentcontainer')[1].getElementsByTagName('h2')[1].style.display = 'none'; \n" +
                    "document.getElementsByClassName('l-navigationscontainer')[0].style.display = 'none'; \n" +
                    "var hrs = document.getElementsByTagName('hr');\n" +
                    "     for(var c=0; c < hrs.length; c++) {\n" +
                    "      if(hrs[c].style) {\n" +
                    "       hrs[c].style.display = 'none';\n" +
                    "      }\n" +
                    "      else{\n" +
                    "       hrs[c].style.display = 'none';\n" +
                    "      }\n" +
                    "     }" +
                    "})()");
            view.loadUrl("javascript:(function() { " +
                    "document.getElementsByClassName('l-contentcontainer')[1].style.width = '100%'; " +
                    "document.getElementsByClassName('l-contentcontainer')[1].style.padding = '0'; " +
                    "})()");
        }
    }

}
