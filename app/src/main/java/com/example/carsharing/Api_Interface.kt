package com.example.carsharing

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Api_Interface {

    @POST("/api/register")
    fun signUp(@Body item:RequestSignup):Call<ResponseSignup>

    @POST("/api/login")
    fun login(@Body item:RequestLogin):Call<ResponseLogin>

    //登出
    @POST("/api/logout")
    fun logout(): Call<ResponseLogout>

    //刊登
    @POST(" /api/post")
    fun post(@Body requestPost: RequestPost): Call<ResponsePost>

    //查看全部文章
    @GET("/api/post")
    fun getAll(@Query ("row") row:Int): Call<ResponseAllposts>
}