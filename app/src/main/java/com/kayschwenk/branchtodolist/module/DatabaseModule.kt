package com.kayschwenk.branchtodolist.module

import android.content.Context
import androidx.room.Room
import com.kayschwenk.branchtodolist.data.ToDoDatabase
import com.kayschwenk.branchtodolist.data.ToDoItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideToDoItemDao(toDoDatabase: ToDoDatabase): ToDoItemDao {
        return toDoDatabase.toDoItemDao()
    }

    @Provides
    @Singleton
    fun provideToDoDatabase(@ApplicationContext appContext: Context): ToDoDatabase {
        return Room.databaseBuilder(
            appContext,
            ToDoDatabase::class.java,
            "todo_database"
        ).build()
    }
}