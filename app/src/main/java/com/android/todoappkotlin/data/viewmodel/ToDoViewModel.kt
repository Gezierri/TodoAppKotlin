package com.android.todoappkotlin.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.todoappkotlin.data.ToDoDatabase
import com.android.todoappkotlin.data.model.ToDoData
import com.android.todoappkotlin.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val repository: ToDoRepository = ToDoRepository(toDoDao)

    val getAllData: LiveData<List<ToDoData>> = repository.getAllData

    fun insert(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun update(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun delete(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }

}