package com.kayschwenk.branchtodolist.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM toDoItem")
    fun getAll(): Flow<List<ToDoItem>>

    @Insert
    fun insertAll(vararg toDoItems: ToDoItem)

    @Delete
    fun delete(toDoItem: ToDoItem)
}

@Entity
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "task") val task: String
)