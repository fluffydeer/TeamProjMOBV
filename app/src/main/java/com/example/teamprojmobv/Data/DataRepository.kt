
package com.example.teamprojmobv.Data

import androidx.lifecycle.LiveData
import com.example.teamprojmobv.Data.db.LocalCache
import com.example.viewmodel.data.db.model.UserItem
import com.opinyour.android.app.data.api.WebApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.net.ConnectException


/**
 * Repository class that works with local and remote data sources.
 */
class DataRepository private constructor(
    private val api: WebApi,
    private val cache: LocalCache
) {

    companion object {
        const val TAG = "DataRepository"
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(api: WebApi, cache: LocalCache): DataRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: DataRepository(api, cache).also { INSTANCE = it }
            }
    }

     fun getActualUser(): LiveData<UserItem> = cache.getActualUser()


    suspend fun createUser(
        action: String,
        apikey: String,
        email: String,
        username: String,
        password: String
    ) {

        try {
            val jsonObject = JSONObject()
            jsonObject.put("action", action)
            jsonObject.put("apikey", apikey)
            jsonObject.put("email", email)
            jsonObject.put("username", username)
            jsonObject.put("password", password)
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            val response = api.createUser(requestBody)
            if (response.isSuccessful) {
                response.body()?.let {

                    //cache.deleteUsers()
                    return cache.insertUser(
                        UserItem(it.id, it.username, it.email, it.token, it.refresh, it.profile)
                        )
                    }
                }
        } catch (ex: ConnectException) {

            ex.printStackTrace()
            return
        } catch (ex: Exception) {
            ex.printStackTrace()
            return
        }
    }

    suspend fun loginUser(
        action: String,
        apikey: String,
        username: String,
        password: String
    ) {

        try {
            val jsonObject = JSONObject()
            jsonObject.put("action", action)
            jsonObject.put("apikey", apikey)
            jsonObject.put("username", username)
            jsonObject.put("password", password)
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            val response = api.loginUser(requestBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    //cache.deleteUsers()
                    return cache.insertUser(
                        UserItem(it.id, it.username, it.email, it.token, it.refresh, it.profile)
                    )
                }
            }
        } catch (ex: ConnectException) {
            ex.printStackTrace()
            return
        } catch (ex: Exception) {
            ex.printStackTrace()
            return
        }
    }


}

