package com.example.playstorecollection.callbacks

import android.util.Log
import android.webkit.JavascriptInterface

class AppsJavaScriptInterface(private val callback: ApiResponseCallback) {
    private var callbacks: ApiResponseCallback? = null

  /*  fun AppsJavaScriptInterface(callback: ApiResponseCallback) {
        callbacks = callback
    }*/

    @JavascriptInterface
    public fun sendGetGroupsRequest(message: String) {
        // Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        // var valueReceived =  document.getElementById("inputField").value;
        // JSBridge.showMessageInNative(valueReceived);
        Log.d("webvdata", message)
        callback.OnDataReceived(message)
    }

    @JavascriptInterface
    public fun getWatchListSymbols(message: String) {

        Log.d("webvdatchListSymbols", message)
        //callback.OnDataReceived(message)
    }

    @JavascriptInterface
    public fun sendGetSymbolsRequest(message: String) {
        Log.d("webvdatchListSymbols2", message)
        callback.onApiResponse(message)
    }

    @JavascriptInterface
    public fun sendSearchSymbolsRequest(message: String) {
        Log.d("webvdatchSearch", message)
        callback.OnSearchData(message)
    }
    @JavascriptInterface
    public fun sendTheme(message: String){
        Log.d("themesend", message)
        callback.onThemeData(message)
    }

}