package com.example.playstorecollection;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.BuildConfig;

import java.util.ArrayList;
import java.util.List;

public class WebviewActivity extends AppCompatActivity {
   String appname = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView view = this.findViewById(R.id.webview);


        // For LIVE: Remove this block
        if (BuildConfig.DEBUG)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        Intent intent = getIntent();
        appname = intent.getStringExtra("appName");

        view.getSettings().setDomStorageEnabled(true);
        view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        // view.addJavascriptInterface(new NativeAndWebBridgeInterface(this), "Android");
        view.getSettings().setJavaScriptEnabled(true);

        /*AppsJavaScriptInterface interfaceInstance = new AppsJavaScriptInterface(this);
        view.addJavascriptInterface(interfaceInstance, "Android");
*/


        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

               // view.loadUrl(url);
                Log.d("webvdview", url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                view.evaluateJavascript(url, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        // 'value' contains the result of the JavaScript code
                        Log.d("WebdView", "Data: " + value);
                    }
                });

                Log.d("webddata", "onpagefinished");
                List<String> dataList = new ArrayList<>();
                dataList.add("Item 1");
                dataList.add("Item 2");
                dataList.add("Item 3");

                String jsonData = "{" +
                        "    \"data\": {" +
                        "        \"watchlists\": [" +
                        "            {" +
                        "                \"wName\": \"new\"," +
                        "                \"wId\": \"new\"" +
                        "            }," +
                        "            {" +
                        "                \"wName\": \"WL 1\"," +
                        "                \"wId\": \"wl1\"" +
                        "     " +
                        "" +
                        "" +
                        "       }" +
                        "        ]" +
                        "    }," +
                        "    \"status\": true" +
                        "}";
                String data = "Hello from Android!";
                String javascriptCode2 = "getGroupsResponse('" + jsonData + "');";
                /*webView.post(() -> {
                    webView.evaluateJavascript(javascriptCode2, null);
                });*/

                view.evaluateJavascript(javascriptCode2, null);
                //view.evaluateJavascript("getGroupsResponse('message read')",null);
            }

        });
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.getSettings().setAllowUniversalAccessFromFileURLs(true);
            CookieManager cookieManager = CookieManager.getInstance();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.setAcceptThirdPartyCookies(view, true);
            }
        }*/

        String filename = appname.replace(".zip","");
       // view.loadUrl("file:///" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/unzipped"+ appname +"/build/index.html");
       // view.loadUrl("file:///" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/unzippedAppNew/" +filename +"/web/index.html");
      //  view.loadUrl("file:///" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/unzippedAppNew/" +filename +"/index.html");
        view.loadUrl("file:///"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/unzippedNew/build/index.html");

    }


}

