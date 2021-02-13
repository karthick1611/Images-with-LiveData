package com.online.liveimages.restClient

import com.google.gson.GsonBuilder
import com.online.liveimages.model.GetImages
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

object RestClient {

    private const val BASE_URL="https://api.pexels.com/v1/"
    private const val GET_IMAGES="search?query=people"

    private val retrofitBuilder:Retrofit.Builder by lazy {

        val defaultHttpClient = OkHttpClient.Builder()
            .connectTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "YOUR API KEY")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }.build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(defaultHttpClient)
            .addConverterFactory(GsonConverterFactory.create(
                GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create()))
    }

    val apiService:ApiService by lazy {
        retrofitBuilder.build().create(ApiService::class.java)
    }

    interface ApiService {

        @GET(GET_IMAGES)
        suspend fun getImages(): GetImages

    }
}