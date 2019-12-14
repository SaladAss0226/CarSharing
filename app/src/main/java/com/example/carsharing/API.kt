package com.example.carsharing

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object API {
    var token: String? = null
    val apiInterface: Api_Interface by lazy {  //需要使用到時，只會初始化一次
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)
        val myOkHttpClient = OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val requestBuilder = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header("Accept", "application/json")
                    if(token != null){
                        requestBuilder.addHeader("Authorization","Bearer $token")
                    }
                    return chain.proceed(requestBuilder.build())
                }
            })
            .build()
        //retrofit實體
        return@lazy Retrofit.Builder()
            .baseUrl("http://35.194.168.18")
            .client(myOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api_Interface::class.java)
    }
}

