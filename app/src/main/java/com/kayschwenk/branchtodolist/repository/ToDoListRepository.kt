package com.kayschwenk.branchtodolist.repository

import com.kayschwenk.branchtodolist.data.ToDoItem
import com.kayschwenk.branchtodolist.data.ToDoItemDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ToDoListRepositoryImpl @Inject constructor(
    private val dao: ToDoItemDao
): ToDoListRepository {

    override suspend fun writeToDo(task: String) {
         withContext(Dispatchers.IO){
            dao.insertAll(ToDoItem(task = task))
        }
    }

    override fun readToDos(): Flow<List<ToDoItem>> {
        return dao.getAll()
    }

    override suspend fun deleteToDo(toDoItem: ToDoItem) {
        withContext(Dispatchers.IO){
            dao.delete(toDoItem)
        }
    }
}

interface ToDoListRepository {
    suspend fun writeToDo(task: String)
    fun readToDos(): Flow<List<ToDoItem>>
    suspend fun deleteToDo(toDoItem: ToDoItem)
}