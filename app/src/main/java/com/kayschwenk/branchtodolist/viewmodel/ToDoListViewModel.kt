package com.kayschwenk.branchtodolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kayschwenk.branchtodolist.data.ToDoItem
import com.kayschwenk.branchtodolist.repository.ToDoListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val repository: ToDoListRepository
): ViewModel() {

    private val _toDoItems = MutableStateFlow<List<ToDoItem>>(listOf())
    val toDoItems = _toDoItems.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun createToDo(task: String) {
        viewModelScope.launch {
            repository.writeToDo(task)
        }
    }

    fun fetchToDos() {
        repository
            .readToDos()
            .onEach { _toDoItems.value = it }
            .catch { _error.value = it.message }
            .launchIn(viewModelScope)
    }

    fun clearToDo(toDoItem: ToDoItem) {
        viewModelScope.launch {
            repository.deleteToDo(toDoItem)
        }
    }
}