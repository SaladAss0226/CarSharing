package com.example.carsharing

import retrofit2.Call
import retrofit2.http.POST

interface Api_Interface {

    //登出
    @POST("/api/logout")
    fun logout(): Call<ResponseLogout>
    
}