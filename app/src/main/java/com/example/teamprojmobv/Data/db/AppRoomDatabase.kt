package com.example.teamprojmobv.Data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.viewmodel.data.db.model.UserItem

@Database(
    entities = [UserItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun appDao(): DbDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppRoomDatabase::class.java, "users.db"
            ).fallbackToDestructiveMigration()
                .build()
    }
}