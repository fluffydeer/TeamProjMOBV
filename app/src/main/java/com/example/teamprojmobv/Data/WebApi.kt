package com.opinyour.android.app.data.api

import android.content.Context
import com.example.teamprojmobv.Data.db.model.ExistsItem
import com.example.viewmodel.data.db.model.UserItem
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface WebApi {
    @Multipart
    @POST("/mobv/upload.php")
    suspend fun createPhoto(
        @Part image: MultipartBody.Part,
        @Part("data") image_json: RequestBody
    ): Response<ResponseBody>

    @Multipart
    @POST("/mobv/upload.php")
    suspend fun createVideo(
        @Part image: MultipartBody.Part,
        @Part("data") video_json: RequestBody
    ): Response<ResponseBody>

    @POST("/mobv/service.php")
    suspend fun createUser(@Body requestBody: RequestBody): Response<UserItem>

    @POST("/mobv/service.php")
    suspend fun loginUser(@Body requestBody: RequestBody): Response<UserItem>

    @POST("/mobv/service.php")
    suspend fun changePassword(@Body requestBody: RequestBody): Response<UserItem>

    /* @GET("/mobv/service.php")
    suspend fun existsUser(@Header("Content-Type") "application/json" : String, @Body requestBody: RequestBody): Response<ExistsItem>*/

    /*@GET("/mobv/service.php")
    suspend fun existsUser(@Query("action") action: String, @Query("apikey") apikey: String, @Query("username") username: String): Response<ExistsItem>*/

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