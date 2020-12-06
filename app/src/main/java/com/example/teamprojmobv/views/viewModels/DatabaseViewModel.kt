package com.example.teamprojmobv.views.viewModels


import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.example.teamprojmobv.Data.ApiConstants
import com.example.teamprojmobv.Data.DataRepository
import com.example.teamprojmobv.R
import com.example.viewmodel.data.db.model.UserItem
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DatabaseViewModel(private val repository: DataRepository) : ViewModel() {
    // mutable kvoli errom ak napriklad nevyplni
    val email: MutableLiveData<String> = MutableLiveData()
    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()


    /*val actualUsers: LiveData<List<UserItem>>
        get() = repository.getActualUsers()*/

    val getActualUser: LiveData<UserItem>
        get() = repository.getActualUser()

     val successRes :MutableLiveData<Boolean> = MutableLiveData<Boolean>()


    //val text: LiveData<String> = Transformations.map(actualUser) { it.toString() }
    //val loggedUser: LiveData<UserItem> = Transformations.map(actualUsers) { it.first() }

    fun register() {
        viewModelScope.launch {
            if(repository.existsUser(ApiConstants.EXISTS_CONST,ApiConstants.API_KEY, (username.value!!)))
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

    // ako logout??
    fun deleteUsers() {
        viewModelScope.launch {
            repository.deleteUsers()
        }
    }

    fun addUserVideo(filePath: String, apikey: String) {
        viewModelScope.launch {
            repository.uploadVideo(filePath, apikey)
        }
    }

    fun changePassword(pwd : String):Boolean{
        //runBlocking - caka to na return premennu z korutiny
        val result = runBlocking {
            repository.changePassword("password", "yS9zD3dI4uR2aK0cY9cS5pT6tK2rZ6", pwd)
        }
        return result
    }

    fun getLoggedUser() : LiveData<UserItem>{
        return repository.getActualUser()
    }

    fun getUserInfo(): UserItem {
        return repository.getLoggedUser()
    }

    fun getPassword():String{
        return repository.getPassword()
    }
}
