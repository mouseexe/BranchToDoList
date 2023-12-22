package com.kayschwenk.branchtodolist.module

import com.kayschwenk.branchtodolist.repository.ToDoListRepository
import com.kayschwenk.branchtodolist.repository.ToDoListRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindToDoListRepository(repository: ToDoListRepositoryImpl): ToDoListRepository
}