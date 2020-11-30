package com.example.teamprojmobv.Views.ViewModels


import androidx.lifecycle.*
import com.example.teamprojmobv.Data.DataRepository

import kotlinx.coroutines.launch

class DatabaseViewModel(private val repository: DataRepository) : ViewModel() {

    fun register(action: String, apikey: String, email:String, username:String, password:String) {
        viewModelScope.launch {
            repository.createUser(action, apikey, email, username, password)
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
