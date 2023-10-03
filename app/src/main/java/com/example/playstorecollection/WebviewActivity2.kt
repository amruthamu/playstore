package com.example.playstorecollection

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.BuildConfig
import com.example.playstorecollection.callbacks.ApiResponseCallback
import com.example.playstorecollection.callbacks.AppsJavaScriptInterface
import com.example.playstorecollection.callbacks.WatchlistApiServices
import com.example.playstorecollection.model.*
import com.example.playstorecollection.utills.MyName
import com.example.playstorecollection.utills.ThemeData
import com.example.playstorecollection.utills.ThemeSwitchState
import com.example.playstorecollection.utills.UseNameState
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main2.progressBar
import kotlinx.android.synthetic.main.activity_webview2.*
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


class WebviewActivity2 : AppCompatActivity(), ApiResponseCallback {

    var appname = ""
    lateinit var responseData: WatchlistData
    lateinit var symbolResponseData: SymbolData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview2)
        supportActionBar?.hide()
        val intent = intent
        appname = intent.getStringExtra("appName").toString()
        var filename = appname.replace(".zip","")
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // // Replace 'R.id.toolbar' with the ID of your Toolbar
      if(appname=="uuuu"){
          text1.text = "Activity"
       }else{
          text1.text = appname
       }
        toolbar_image.setOnClickListener {
            finish()
        }

        //getWatchlistData()
        //getWatchlistSymbolData("new")
        //getWatchlistSearchSymbolData("aa")
        // For LIVE: Remove this block
        if (BuildConfig.DEBUG) if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        webview.getSettings().setDomStorageEnabled(true)
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAllowContentAccess(true);
        WebView.setWebContentsDebuggingEnabled(true);
        // view.addJavascriptInterface(new NativeAndWebBridgeInterface(this), "Android");
        // view.addJavascriptInterface(new NativeAndWebBridgeInterface(this), "Android");
        webview.getSettings().setJavaScriptEnabled(true)
       webview.settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.clearCache(true)

        // view.loadUrl("file:///" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/unzipped"+ appname +"/build/index.html");
        // view.loadUrl("file:///" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/unzippedAppNew/" +filename +"/web/index.html");
        //  view.loadUrl("file:///" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/unzippedAppNew/" +filename +"/index.html");
       // webview.loadUrl("file:///" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/unzippedNew/build/index.html")
        webview.loadUrl("file:///" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/unzippedAppNew/"+filename +"/build/index.html")
        //webview.loadUrl("file:///" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/index.html")
        // webview.loadUrl("https://karthickoff.github.io/superAppPoc/")
        //Log.d("webvdview", "https://karthickoff.github.io/superAppPoc/")

        val interfaceInstance = AppsJavaScriptInterface(this)
        webview.addJavascriptInterface(interfaceInstance, "Android")

        webview.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                // view.loadUrl(url);
                Log.d("webvdview", url)
                return true
            }
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progress_bar.visibility = View.VISIBLE
            }
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                Log.e("WebViewErrorammu", "Error: ${error?.description}")
                // Show the progress bar for errors
                progress_bar.visibility = View.VISIBLE
            }
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                progress_bar.visibility=View.GONE
                /* view.evaluateJavascript(url) { value -> // 'value' contains the result of the JavaScript code
                     Log.d("WebdView", "Data: $value")
                 }*/
                val json = JSONObject()
                json.put("message", "Hello from Android!")
                val javascriptCode = "handlePageFinished('$json');"
                webview.evaluateJavascript(javascriptCode, null)

                Log.d("webddata", "onpagefinished")
                val dataList: MutableList<String> = ArrayList()
                dataList.add("Item 1")
                dataList.add("Item 2")
                dataList.add("Item 3")
                val jsonData = "{" +
                        "    \"data\": {" +
                        "        \"watchlists\": [" +
                        "            {" +
                        "                \"wName\": \"new\"," +
                        "                \"wId\": \"new\"" +
                        "            }," +
                        "            {" +
                        "                \"wName\": \"WL 1\"," +
                        "                \"wId\": \"wl1\"" +
                        "            }" +
                        "        ]" +
                        "    }," +
                        "    \"status\": true" +
                        "}"
                val data = "Hello from Android!"

                val symbolsArray = JSONArray()
                val symbol1 = JSONObject()
                symbol1.put("symbol", "DAX")
                symbol1.put("wId", "new")
                symbol1.put("companyName", "Recon Capital DAX Germany ETF")
                symbolsArray.put(symbol1)

                val symbol2 = JSONObject()
                symbol2.put("symbol", "AAIT")
                symbol2.put("wId", "new")
                symbol2.put(
                    "companyName",
                    "iShares MSCI All Country Asia Information Technology Index Fund"
                )
                symbolsArray.put(symbol2)

                val symbol3 = JSONObject()
                symbol3.put("symbol", "AAON")
                symbol3.put("wId", "new")
                symbol3.put("companyName", "AAON, Inc.")
                symbolsArray.put(symbol3)

                val symbol4 = JSONObject()
                symbol4.put("symbol", "MSFT")
                symbol4.put("wId", "new")
                symbol4.put("companyName", "Microsoft Corporation")
                symbolsArray.put(symbol4)

                val symbol5 = JSONObject()
                symbol5.put("symbol", "AAME")
                symbol5.put("wId", "new")
                symbol5.put("companyName", "Atlantic American Corporation")
                symbolsArray.put(symbol5)

                val symbol6 = JSONObject()
                symbol6.put("symbol", "AAL")
                symbol6.put("wId", "new")
                symbol6.put("companyName", "American Airlines Group, Inc.")
                symbolsArray.put(symbol6)

// Create the data object
                val dataObject = JSONObject()
                dataObject.put("symbols", symbolsArray)

// Create the final JSON object
                val jsonObject = JSONObject()
                jsonObject.put("data", dataObject)
                jsonObject.put("status", true)

// Convert the JSON object to a string
                val jsonStr = jsonObject.toString()


                val gson = Gson()
                /*if (::responseData.isInitialized) {
                    val json = gson.toJson(responseData)
                    val javascriptCode2 = "getGroupsResponse('$json');"
                    Log.d("ompfinsd", json.toString())
                    Log.d("ompfinsd2", jsonData.toString())
                    view.evaluateJavascript(javascriptCode2, null)

                } else {
                    Log.d("ompfinsd2 not init", jsonData.toString())
                }*/

                /* if (::symbolResponseData.isInitialized){
                     val json2 = gson.toJson(symbolResponseData)
                     val javascriptCode3 = "getWatchListSymbolsResponse('$json2');"
                     view.evaluateJavascript(javascriptCode3, null)
                 } else {
                     val javascriptCode3 = "getWatchListSymbolsResponse('$jsonStr');"
                     view.evaluateJavascript(javascriptCode3, null)
                     Log.d("symbolRes not init", jsonData.toString())
                 }
 */


                //view.evaluateJavascript("getGroupsResponse('message read')",null);
                /*webView.post(() -> {
                    webView.evaluateJavascript(javascriptCode2, null);
                });*/
            }


        })
    }

    override fun onDestroy() {
        webview.destroy()
        super.onDestroy()
    }

    override fun onPause() {
        webview.onPause()
        super.onPause()
    }

    override fun onResume() {
        webview.onResume()
        super.onResume()
    }

    override fun onApiResponse(response: String?) {
        response?.let { getWatchlistSymbolData(response) }
    }

    override fun OnDataReceived(message: String) {
        getWatchlistData()
    }

    override fun OnSearchData(message: String) {
        getWatchlistSearchSymbolData(message)
    }

    override fun onThemeData(message: String) {
        runOnUiThread{
            getThemeDetails()
        }

    }

    override fun onNameData(message: String) {
        runOnUiThread {
            val jsonData = """
        {"name":"${UseNameState.UserName}"}
        """.trimIndent()
            val gson = Gson()
            val data = gson.fromJson(jsonData, MyName::class.java)
            val jsonD = gson.toJson(data)
            val javascriptCode2 = "getUserData('$jsonD')"
            webview.evaluateJavascript(javascriptCode2, null)
        }
    }

    private fun loadThemeDataJsonFromFile(): ThemeData {
        val jsonData = """
        {
          "themes": [
            {
              "theme": "dark",
              "data": {
       
              }
            },
            {
              "theme": "light",
              "data": {
               
              }
            },
            {
              "theme": "custom",
              "data": {
                "color": "#FFCC70",
                "backgroundColor": "#fff"
              }
            }
          ]
        }
    """.trimIndent()
        val gson = Gson()
        return gson.fromJson(jsonData, ThemeData::class.java)
    }
    private fun getThemeDetails() {
        val themeData = loadThemeDataJsonFromFile()
        Log.d("ThemeSwitch", "isDarkThemeEnableddd = ${ThemeSwitchState.isDarkThemeEnabled}")
        if (ThemeSwitchState.isDarkThemeEnabled) {
            val selectedTheme = themeData.themes.find { it.theme == "dark" }
            if (selectedTheme != null) {
                val dataObject = selectedTheme
                val gson = Gson()
                val jsonData = gson.toJson(dataObject)
                val javascriptCode2 = "getTheme('$jsonData')"
                webview.evaluateJavascript(javascriptCode2, null)

            }
        }else{
            val selectedTheme = themeData.themes.find { it.theme == "light" }
            if (selectedTheme != null) {
                val dataObject = selectedTheme
                val gson = Gson()
                val jsonData = gson.toJson(dataObject)
                val javascriptCode2 = "getTheme('$jsonData')"
                webview.evaluateJavascript(javascriptCode2, null)
            }
        }
    }

    fun getWatchlistSymbolData(message: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiservice.myflow.ai/uSBankPOC/") // Base URL of your API
            .client(OkHttpClient()) // You can customize the OkHttpClient if needed
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter
            .build()
       /* var symMessa = "";
        if(message.contains("new")){
            symMessa = "new"
        } else if (message.contains("wl1")){
            symMessa = "wl1"
        } else if (message.contains("list")){
            symMessa = "list"
        }*/
        val jsonString = message
        val gson = Gson()
        val symbol = gson.fromJson(jsonString, SymbolRequest::class.java)
        val symbolValue = symbol.wId

        val apiService = retrofit.create(WatchlistApiServices::class.java)
        val requestbody = SymbolRequest(symbolValue)
        val call = apiService.getWatchlistSymbols(requestbody)
        Log.d("requestbodyymesa", message.toString())
        Log.d("requestbodyy", requestbody.toString())

        call.enqueue(object : Callback<SymbolData> {
            override fun onResponse(
                call: Call<SymbolData>,
                response: Response<SymbolData>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("retrofitt data symbol", data.toString())


                    if (data != null) {
                        //responseData = data
                        val gson = Gson()
                        symbolResponseData = response.body()!!
                        val json2 = gson.toJson(symbolResponseData)
                        val javascriptCode3 = "getWatchListSymbolsResponse('$json2');"
                        webview.evaluateJavascript(javascriptCode3, null)
                    }
                    // Handle the successful response
                    // Access the data from the response object
                } else {
                    // Handle error
                    Log.d("retrofitt datasym error", response.toString())
                }
                Log.d("retrofitt data body2", response.errorBody().toString())
            }

            override fun onFailure(call: Call<SymbolData?>, t: Throwable) {
                // Handle the error
                t.printStackTrace()
                Log.d("retrofitt data failure", t.toString())
            }
        })

    }

    fun getWatchlistData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiservice.myflow.ai/uSBankPOC/") // Base URL of your API
            .client(OkHttpClient()) // You can customize the OkHttpClient if needed
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter
            .build()

        val apiService = retrofit.create(WatchlistApiServices::class.java)
        val requestbody = WatchlistRequestBody("")
        val call = apiService.getData(requestbody)
        call!!.enqueue(object : Callback<WatchlistData?> {
            override fun onResponse(
                call: Call<WatchlistData?>,
                response: Response<WatchlistData?>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("retrofitt data", data.toString())
                    if (data != null) {
                        responseData = data
                        val gson = Gson()
                        val json = gson.toJson(responseData)
                        val javascriptCode2 = "getGroupsResponse('$json');"
                        Log.d("ompfinsd", json.toString())
                        webview.evaluateJavascript(javascriptCode2, null)

                    }
                    // Handle the successful response
                    // Access the data from the response object
                } else {
                    // Handle error
                    Log.d("retrofitt data error", response.toString())
                }
            }

            override fun onFailure(call: Call<WatchlistData?>, t: Throwable) {
                // Handle the error
                t.printStackTrace()
                Log.d("retrofitt data failure", t.toString())
            }
        })
    }

    fun getWatchlistSearchSymbolData(message: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiservice.myflow.ai/uSBankPOC/") // Base URL of your API
            .client(OkHttpClient()) // You can customize the OkHttpClient if needed
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter
            .build()

        val jsonString = message
        val gson = Gson()
        val symbol = gson.fromJson(jsonString, SearchSymbolRequest::class.java)
        val symbolValue = symbol.symbol

        val apiService = retrofit.create(WatchlistApiServices::class.java)
        val requestbody = SearchSymbolRequest(symbolValue.uppercase())
        val call = apiService.getSearchSymbolData(requestbody)
        Log.d("requestbodyymesasearch", message.toString())
        Log.d("requestbodyysearch", requestbody.toString())

        call.enqueue(object : Callback<SearchSymbolData> {
            override fun onResponse(
                call: Call<SearchSymbolData>,
                response: Response<SearchSymbolData>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("retrofitt search symbol", data.toString())

                    if (data != null) {
                        //responseData = data
                        val gson = Gson()
                       // symbolResponseData = response.body()!!
                        val json2 = gson.toJson(response.body())
                        val searchData = "getSearchSymbolsResponse('$json2');"
                        webview.evaluateJavascript(searchData, null)
                    }
                    // Handle the successful response
                    // Access the data from the response object
                } else {
                    // Handle error
                    Log.d("retrofitt datasym error", response.toString())
                }
                Log.d("retrofitt data body2", response.errorBody().toString())
            }

            override fun onFailure(call: Call<SearchSymbolData?>, t: Throwable) {
                // Handle the error
                t.printStackTrace()
                Log.d("retrofitt data failure", t.toString())
            }
        })

    }
}