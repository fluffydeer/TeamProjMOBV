package com.example.teamprojmobv.views.viewModels

import android.net.Uri
import androidx.lifecycle.*
import com.example.teamprojmobv.data.ApiConstants
import com.example.teamprojmobv.data.DataRepository
import com.example.teamprojmobv.data.db.model.MediaItem
import com.example.viewmodel.data.db.model.UserItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DatabaseViewModel(private val repository: DataRepository) : ViewModel() {
    // mutable kvoli errom ak napriklad nevyplni
    val email: MutableLiveData<String> = MutableLiveData()
    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()

    fun getLoggedUser() : LiveData<UserItem>{
        val result = runBlocking {
            repository.getActualUser()
        }
        return result
    }

    fun getImageUri() : Uri? {
        return repository.getImageUri()
    }
    private var mediaData:ArrayList<MediaItem>? = ArrayList<MediaItem>()


//    fun getMedia(): MutableLiveData<MutableList<MediaItem>>?{
//
//        val pom = mediaData?.value?.get(1)?.videourl
//        return mediaData
//    }

    /*val actualUsers: LiveData<List<UserItem>>
        get() = repository.getActualUsers()*/

    fun setImageUri(uri: Uri){
        repository.setImageUri(uri)
    }

    val successRes :MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    //val text: LiveData<String> = Transformations.map(actualUser) { it.toString() }
    //val loggedUser: LiveData<UserItem> = Transformations.map(actualUsers) { it.first() }


    fun register() {
        viewModelScope.launch {
            //if(!repository.existsUser(ApiConstants.EXISTS_CONST,ApiConstants.API_KEY, (username.value!!)))
                successRes.value = repository.createUser(ApiConstants.REG_CONST,ApiConstants.API_KEY, (email.value!!), (username.value!!), (password.value!!))
        }
    }

    fun login() {
        viewModelScope.launch {
            successRes.value = repository.loginUser(
                ApiConstants.LOGIN_CONST,
                ApiConstants.API_KEY, (username.value!!), (password.value!!))
        }
    }
    fun getVideos() : ArrayList<MediaItem>?{
        val result = runBlocking {
            repository.getVideos(ApiConstants.POSTS_CONST, ApiConstants.API_KEY)
        }
        return result
    }

    // ako logout??
    fun deleteUsers() {
        viewModelScope.launch {
            repository.deleteUsers()
        }
    }


    fun addUserVideo(filePath: String) {
        viewModelScope.launch {
            repository.uploadVideo(filePath, ApiConstants.API_KEY)
        }
    }

    fun changePassword(pwd : String):Boolean{
        //runBlocking - caka to na return premennu z korutiny
        val result = runBlocking {
            repository.changePassword(ApiConstants.PWD_CONST, ApiConstants.API_KEY, pwd)
        }
        return result
    }

    fun getUserInfo(): UserItem {
        return repository.getLoggedUser()
    }

    fun getCurrentPassword():String{
        return repository.getPassword()
    }

    fun loadUserPhoto(filePath: String) : Boolean{
        val result = runBlocking {
            repository.uploadProfilePhoto(filePath, ApiConstants.API_KEY)
        }
        return result
    }

    fun logOutUser(){
        repository.resetUserInfo()
        viewModelScope.launch {
            repository.deleteUsers()
        }
    }


}
