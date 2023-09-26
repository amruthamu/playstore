package com.example.playstorecollection

import DataResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("v3/646405e1-7891-40de-8f55-7d50ac9e51ad")
    fun fetchData(): Call<DataResponse>
}
