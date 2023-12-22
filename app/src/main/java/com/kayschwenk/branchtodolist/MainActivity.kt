package com.kayschwenk.branchtodolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.kayschwenk.branchtodolist.data.ToDoItem
import com.kayschwenk.branchtodolist.view.ToDoListScreen
import com.kayschwenk.branchtodolist.viewmodel.ToDoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<ToDoListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchToDos()

        setContent {
            ToDoListScreen(
                todoList = viewModel.toDoItems.collectAsState(),
                onCreateClick = { task: String -> viewModel.createToDo(task) },
                onClearClick = { toDoItem: ToDoItem -> viewModel.clearToDo(toDoItem) }
            )
        }
    }
}