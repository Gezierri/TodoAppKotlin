package com.android.todoappkotlin.data.repository

import androidx.lifecycle.LiveData
import com.android.todoappkotlin.data.ToDoDao
import com.android.todoappkotlin.data.model.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao){

    val getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()
    val sortByHighPriority: LiveData<List<ToDoData>> = toDoDao.sortByHighPriorities()
    val sortByLowPriority: LiveData<List<ToDoData>> = toDoDao.sortByLowPriorities()

    suspend fun insertData(toDoData: ToDoData){
        toDoDao.insert(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDao.update(toDoData)
    }

    suspend fun delete(toDoData: ToDoData){
        toDoDao.delete(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    fun searchTodo(query: String): LiveData<List<ToDoData>>{
        return toDoDao.searchTodoByName(query)
    }
}