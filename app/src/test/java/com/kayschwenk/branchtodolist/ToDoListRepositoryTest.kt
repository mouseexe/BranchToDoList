package com.kayschwenk.branchtodolist

import com.kayschwenk.branchtodolist.data.ToDoItem
import com.kayschwenk.branchtodolist.data.ToDoItemDao
import com.kayschwenk.branchtodolist.repository.ToDoListRepositoryImpl
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ToDoListRepositoryTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainDispatcherRule()

    private val dao = mockk<ToDoItemDao>(relaxUnitFun = true)
    private val repository = ToDoListRepositoryImpl(dao)

    private val toDoList = listOf(
        ToDoItem(0, "zero"),
        ToDoItem(1, "one"),
        ToDoItem(2, "two")
    )
    private val task = "to do"

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `write to do - success`() = runTest {
        repository.writeToDo(task)
        advanceUntilIdle()
        coVerify { dao.insertAll(ToDoItem(task = task)) }
        confirmVerified(dao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `read to dos - success`() = runTest {
        every { dao.getAll() } returns flowOf(toDoList)
        val list = repository.readToDos()
        advanceUntilIdle()
        assertEquals(list.first(), toDoList)
        coVerify { dao.getAll() }
        confirmVerified(dao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `delete to do - success`() = runTest {
        repository.deleteToDo(toDoList[0])
        advanceUntilIdle()
        coVerify { dao.delete(toDoList[0]) }
        confirmVerified(dao)
    }
}