package com.android.todoappkotlin.data

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.android.todoappkotlin.data.model.ToDoData

@Dao
interface ToDoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<ToDoData>>

    @Insert(onConflict = IGNORE)
    suspend fun insert(toDoData: ToDoData)

    @Update
    suspend fun update(toDoData: ToDoData)

    @Delete
    suspend fun delete(toDoData: ToDoData)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM todo_table WHERE title LIKE :query")
    fun searchTodoByName(query: String): LiveData<List<ToDoData>>

    @Query("SELECT * FROM todo_table ORDER BY CASE " +
            "WHEN priority LIKE '%H%' THEN 1 " +
            "WHEN priority LIKE '%M%' THEN 2 " +
            "WHEN priority LIKE '%L%' THEN 3 END")
    fun sortByHighPriorities(): LiveData<List<ToDoData>>

    @Query("SELECT * FROM todo_table ORDER BY CASE " +
            "WHEN priority LIKE '%L%' THEN 1 " +
            "WHEN priority LIKE '%M%' THEN 2 " +
            "WHEN priority LIKE '%H%' THEN 3 END")
    fun sortByLowPriorities(): LiveData<List<ToDoData>>
}