package com.example.playstorecollection.callbacks

import com.example.playstorecollection.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WatchlistApiServices {

    @POST("watchlist/getGroups")
    fun getData(@Body requestBody: Any): Call<WatchlistData?>?

    @POST("watchlist/getSymbols")
    fun getWatchlistSymbols(@Body requestBody: Any): Call<SymbolData>

    @POST("symbols/searchSymbol")
    fun getSearchSymbolData(@Body requestBody: Any) : Call<SearchSymbolData>
}