package com.example.teamprojmobv.Data.db

import androidx.lifecycle.LiveData
import com.example.viewmodel.data.db.model.UserItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LocalCache(private val dao: DbDao) {

    /*suspend fun insertUser(wordItems: List<UserItem>) {
        dao.insertUser(wordItems)
    }*/

    suspend fun insertUser(userItem: UserItem) {
        dao.insertUser(userItem)
    }

    suspend fun updateUser(userItem: UserItem) {
        dao.updateUser(userItem)
    }

    fun deleteUser(userItem: UserItem) {
        GlobalScope.launch { dao.deleteUser(userItem) }
    }

    fun getUser(): LiveData<List<UserItem>> = dao.getUser()

}