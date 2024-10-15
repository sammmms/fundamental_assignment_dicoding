package com.example.myfirstsubmission.data.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("events?active=1&limit=1")
    fun getLatestActiveEvents(): Call<ListResponse>

    @GET("events?active=1")
    fun getActiveEvents(@Query("q") q:String = "", @Query("limit") limit: String = "40"): Call<ListResponse>

    @GET("events?active=0")
    fun getCompletedEvents(@Query("q") q:String = "", @Query("limit") limit: String = "40"): Call<ListResponse>

}