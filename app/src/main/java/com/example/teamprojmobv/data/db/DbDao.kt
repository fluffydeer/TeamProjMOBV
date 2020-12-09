package com.example.teamprojmobv.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.viewmodel.data.db.model.UserItem


@Dao
interface DbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userItem: UserItem)

    @Query("DELETE FROM users")
    suspend fun deleteUsers()

    @Query("SELECT * FROM users ORDER BY timestamp LIMIT 1")
    fun getActualUsers(): LiveData<List<UserItem>>

    @Query("SELECT * FROM users ORDER BY timestamp LIMIT 1")
    fun getActualUser(): LiveData<UserItem>

   /* @Update
    suspend fun updateUser(userItem: UserItem)

    @Delete
    suspend fun deleteUser(userItem: UserItem)

    @Query("DELETE FROM users")
    fun deleteUsers()

    @Query("SELECT * FROM users")
    fun getUser(): LiveData<List<UserItem>>

    @Query("SELECT * FROM users")
    fun getActualUser(): LiveData<UserItem>
*/
}