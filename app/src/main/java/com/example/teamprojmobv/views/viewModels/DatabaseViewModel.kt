package com.example.teamprojmobv.views.viewModels


import androidx.lifecycle.*
import com.example.teamprojmobv.Data.DataRepository
import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: DataRepository) : ViewModel() {
    val email: MutableLiveData<String> = MutableLiveData()
    //val welcome = Transformations.map(email) { w -> "Slovo je: $w" }
    val username: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()


    //fun register(action: String, apikey: String, email:String, username:String, password:String) {
    fun register(action: String, apikey: String) {
        viewModelScope.launch {
            repository.createUser(action, apikey, (email.value!!), (username.value!!), (password.value!!))
        }
    }

    fun login(action: String, apikey: String) {
        viewModelScope.launch {
            repository.loginUser(action, apikey, (username.value!!), (password.value!!))
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
