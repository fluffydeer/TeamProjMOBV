
package com.example.teamprojmobv.Data

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.teamprojmobv.Data.db.LocalCache
import com.example.teamprojmobv.Data.util.ChCrypto
import com.example.viewmodel.data.db.model.UserItem
import com.opinyour.android.app.data.api.WebApi
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.net.ConnectException



/**
 * Repository class that works with local and remote data sources.
 */
class DataRepository private constructor(
    private val api: WebApi,
    private val cache: LocalCache
) {

    private lateinit var token: String
    private lateinit var pwd: String
    private lateinit var loggedUser : UserItem

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

    fun getActualUsers(): LiveData<List<UserItem>> = cache.getActualUsers()
    fun getActualUser(): LiveData<UserItem> = cache.getActualUser()
    fun deleteUsers() = cache.deleteUsers()
    fun getPassword():String = pwd

    //tu by som asi mala tahat prihlaseneho uzivatela z cache vsak na co to mame
    fun getLoggedUser():UserItem{
        return loggedUser
    }

    suspend fun createUser(
        action: String,
        apikey: String,
        email: String,
        username: String,
        password: String
    ):Boolean {

        try {
            val jsonObject = JSONObject()
            jsonObject.put("action", action)
            jsonObject.put("apikey", apikey)
            jsonObject.put("email", email)
            jsonObject.put("username", username)
            // password zadava pouzivatel, SYM_ENC_KEY je 32 char string
            val passwordEnc = ChCrypto.aesEncrypt(password, ApiConstants.SYM_ENC_KEY)
            jsonObject.put("password", passwordEnc)
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            val response = api.createUser(requestBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    val currentTimestamp = System.currentTimeMillis()
                    loggedUser = UserItem(it.id, it.username, it.email, it.token, it.refresh, it.profile, currentTimestamp)
                    token = it.token
                    pwd = password
                    Log.i("DataRepository", token + " " + pwd)
                    cache.insertUser(
                        UserItem(
                            it.id,
                            it.username,
                            it.email,
                            it.token,
                            it.refresh,
                            it.profile,
                            currentTimestamp
                        )
                    )
                    return true
                    }
                }
        } catch (ex: ConnectException) {

            ex.printStackTrace()
            return false
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }
        return false
    }

    suspend fun loginUser(
        action: String,
        apikey: String,
        username: String,
        password: String
    ): Boolean {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("action", action)
            jsonObject.put("apikey", apikey)
            jsonObject.put("username", username)
            val passwordEnc = ChCrypto.aesEncrypt(password, ApiConstants.SYM_ENC_KEY)
            jsonObject.put("password", passwordEnc)
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            val response = api.loginUser(requestBody)
            if (response.isSuccessful) {
                response.body()?.let {
                     //cache.deleteUsers()
                    val currentTimestamp = System.currentTimeMillis()
                    token = it.token
                    pwd = password
                    loggedUser = UserItem(it.id, it.username, it.email, it.token, it.refresh, it.profile, currentTimestamp)
                    cache.insertUser(
                         UserItem(it.id, it.username, it.email, it.token, it.refresh, it.profile, currentTimestamp)
                     )
                    Log.i("DataRepository", token + " " + pwd)
                    return true
                }
            }
        } catch (ex: ConnectException) {
            ex.printStackTrace()
            return false
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }
        return false
    }

    suspend fun existsUser(existsConst: String, apiKey: String, value: String): Boolean {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("action", existsConst)
            jsonObject.put("apikey", apiKey)
            jsonObject.put("username", value)
            val jsonObjectString = jsonObject.toString()
            // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
            val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

            val response = api.existsUser(requestBody)
            return response

        } catch (ex: ConnectException) {
            ex.printStackTrace()
            return true
        } catch (ex: Exception) {
            ex.printStackTrace()
            return true
        }

    }

    suspend fun uploadVideo(
        filePath: String,
        apikey: String,
    ) {
        try {
            val file = File(filePath)

            val jsonData = JSONObject()

            //token = logged
            jsonData.put("apikey", apikey)
            jsonData.put("token", token)
            val jsonDataString = jsonData.toString()
//            val json= RequestBody.create(MediaType.parse("application/json"), jsonDataString)
//            val video = RequestBody.create(MediaType.parse("video/*"), file)
//            val filePart =
//                MultipartBody.Part.createFormData("video", "video" ,video)
//
//            val responseVideo = api.createVideo(filePart, json)
//
//            Log.i("DataRepository upload", responseVideo.toString())
//            Log.i("DataRepository upload", responseVideo.body().toString())
//            if (responseVideo.isSuccessful) {
//                responseVideo.body()?.let {
//                    Log.i("DataRepository upload", it.string())
//                    return
//                }
//            }
        } catch (ex: ConnectException) {
            ex.printStackTrace()
            return
        } catch (ex: Exception) {
            Log.e("DataRepository upload", ex.toString())
            ex.printStackTrace()
            return
        }
    }



    suspend fun changePassword(
        action: String,
        apikey: String,
        newPwd: String
    ) : Boolean{
        try {
            val jsonObject = JSONObject()
            jsonObject.put("action", action)
            jsonObject.put("apikey", apikey)
            jsonObject.put("token", token)
            jsonObject.put("oldpassword", ChCrypto.aesEncrypt(pwd, ApiConstants.SYM_ENC_KEY))
            jsonObject.put("newpassword", ChCrypto.aesEncrypt(newPwd, ApiConstants.SYM_ENC_KEY))
            val jsonObjectString = jsonObject.toString()
            val requestBody=
                jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())
            val response = api.changePassword(requestBody)

            Log.i("changepassword", response.toString())  //{protocol=http/1.1, code=200, message=OK... vratilo request

            if (response.isSuccessful) {
                response.body()?.let {
                    Log.i("changepassword", "old: " + pwd + " new: " + newPwd)
                    pwd = newPwd
                    token = it.token
                    val currentTimestamp = System.currentTimeMillis()
                    // TODO bohvie ci toto funguje
                    // cache.deleteUsers()   //nejde lebo java.lang.IllegalStateException: Cannot access
                    // database on the main thread since it may potentially lock the UI for a long period of time.
                    loggedUser = UserItem(it.id, it.username, it.email, it.token, it.refresh, it.profile, currentTimestamp)
                    cache.insertUser(
                        UserItem(it.id, it.username, it.email, it.token, it.refresh, it.profile, currentTimestamp)
                    )
                    return true
                }
            }
            return false
        } catch (ex: ConnectException) {
            ex.printStackTrace()
            return false
        } catch (ex: Exception) {
            ex.printStackTrace()
            return false
        }
    }
}

