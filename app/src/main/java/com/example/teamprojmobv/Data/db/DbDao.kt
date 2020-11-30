package com.example.teamprojmobv.Data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.viewmodel.data.db.model.UserItem


@Dao
interface DbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userItem: UserItem)

    @Update
    suspend fun updateUser(wordItem: UserItem)

    @Delete
    suspend fun deleteUser(wordItem: UserItem)

    @Query("SELECT * FROM users")
    fun getUser(): LiveData<List<UserItem>>

}