package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WatchlistEntity::class], version = 1, exportSchema = false)
abstract class CineVerseDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao

    companion object {
        @Volatile
        private var INSTANCE: CineVerseDatabase? = null

        fun getDatabase(context: Context): CineVerseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CineVerseDatabase::class.java,
                    "cineverse_global.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
