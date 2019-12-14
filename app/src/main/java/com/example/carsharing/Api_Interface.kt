package com.example.carsharing

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api_Interface {

@POST("/api/register")
fun signUp(@Body item:RequestSignup):Call<ResponseSignup>

    @POST("/api/login")
    fun login(@Body item:RequestLogin):Call<ResponseLogin>





}