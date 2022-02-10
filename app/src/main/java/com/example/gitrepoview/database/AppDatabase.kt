package com.example.gitrepoview.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gitrepoview.model.RepoItem

@Database(entities = [RepoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun repoDao(): RepositoryDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java, "repo_db.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}