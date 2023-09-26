package com.example.playstorecollection.callbacks

interface ApiResponseCallback {
    fun onApiResponse(response: String?)
    fun OnDataReceived(message: String)
    fun OnSearchData(message: String)
    fun onThemeData(message: String)

}