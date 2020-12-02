package com.example.teamprojmobv.views.viewModels


import androidx.lifecycle.*
import androidx.navigation.findNavController
import com.example.teamprojmobv.Data.ApiConstants
import com.example.teamprojmobv.Data.DataRepository
import com.example.teamprojmobv.R
import com.example.viewmodel.data.db.model.UserItem
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: DataRepository) : ViewModel() {
    val email: MutableLiveData<String> = MutableLiveData()
    //val welcome = Transformations.map(email) { w -> "Slovo je: $w" }
    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()


    val actualUsers: LiveData<List<UserItem>>
        get() = repository.getActualUsers()

    val text: LiveData<String> = Transformations.map(actualUsers) { it.toString() }
    val loggedUser: LiveData<UserItem> = Transformations.map(actualUsers) { it[0] }

    //fun register(action: String, apikey: String, email:String, username:String, password:String) {
    fun register() {
        viewModelScope.launch {
            if(repository.existsUser(ApiConstants.EXISTS_CONST,ApiConstants.API_KEY, (username.value!!)))
                repository.createUser(ApiConstants.REG_CONST,ApiConstants.API_KEY, (email.value!!), (username.value!!), (password.value!!))
        }
    }

    fun login() {
        viewModelScope.launch {
            repository.loginUser(
                ApiConstants.LOGIN_CONST,
                ApiConstants.API_KEY, (username.value!!), (password.value!!))
        }
    }

    fun deleteUsers() {
        viewModelScope.launch {
            repository.deleteUsers()
        }
    }
    /*
    val input: MutableLiveData<String> = MutableLiveData()

    val text: LiveData<String> = Transformations.map(repository.getWords()) { it.toString() }

    fun insertWord() {
        input.value?.let {
            if (it.isNotEmpty()) {
                viewModelScope.launch {
                    repository.insertWord(WordItem(it))
                    input.postValue("")
                }
            }
        }
    }*/
}
