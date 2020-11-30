package com.example.viewmodel.data.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserItem(@PrimaryKey val id: Int,
                    val username: String,
                    val email: String,
                    val token: String,
                    val refresh: String,
                    val profile: String) {
    override fun toString(): String {
        return "UserItem(id=$id, username='$username', email='$email', token='$token', refresh='$refresh', profile='$profile')"
    }

}