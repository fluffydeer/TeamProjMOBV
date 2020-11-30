package com.opinyour.android.app.data.api

import android.content.Context
import com.example.viewmodel.data.db.model.UserItem
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WebApi {

    // Raw JSON
    @POST("/mobv/service.php")
    suspend fun createUser(@Body requestBody: RequestBody): Response<UserItem>

    // Raw JSON
    @POST("/mobv/service.php")
    suspend fun loginUser(@Body requestBody: RequestBody): Response<ResponseBody>
    //@GET("realestate")
    //suspend fun getProperties(): Response<List<MarsResponse>>

    companion object {
        private const val BASE_URL =
            "http://api.mcomputing.eu/"

        fun create(context: Context): WebApi {

            val client = OkHttpClient.Builder()
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WebApi::class.java)
        }
    }
}