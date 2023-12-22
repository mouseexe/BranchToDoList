package com.kayschwenk.branchtodolist

import com.kayschwenk.branchtodolist.data.ToDoItem
import com.kayschwenk.branchtodolist.repository.ToDoListRepository
import com.kayschwenk.branchtodolist.viewmodel.ToDoListViewModel
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ToDoListViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val repository = mockk<ToDoListRepository>(relaxUnitFun = true)
    private val viewModel = ToDoListViewModel(repository)

    private val toDoList = listOf(
        ToDoItem(0, "zero"),
        ToDoItem(1, "one"),
        ToDoItem(2, "two")
    )
    private val task = "to do"

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `create to-do - success`() = runTest {
        viewModel.createToDo(task)
        advanceUntilIdle()
        assertEquals(viewModel.error.value, null)
        coVerify { repository.writeToDo(task) }
        confirmVerified(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetch to-dos - success`() = runTest {
        every { repository.readToDos() } returns flowOf(toDoList)
        viewModel.fetchToDos()
        advanceUntilIdle()
        assertEquals(viewModel.toDoItems.value, toDoList)
        assertEquals(viewModel.error.value, null)
        coVerify { repository.readToDos() }
        confirmVerified(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetch to-dos - error`() = runTest {
        val errorMessage = "oh no"
        every { repository.readToDos() } returns flow { throw Exception(errorMessage) }
        viewModel.fetchToDos()
        advanceUntilIdle()
        assertEquals(viewModel.error.value, errorMessage)
        coVerify { repository.readToDos() }
        confirmVerified(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `delete to-do - success`() = runTest {
        viewModel.clearToDo(toDoList[0])
        advanceUntilIdle()
        assertEquals(viewModel.error.value, null)
        coVerify { repository.deleteToDo(toDoList[0]) }
        confirmVerified(repository)
    }
}