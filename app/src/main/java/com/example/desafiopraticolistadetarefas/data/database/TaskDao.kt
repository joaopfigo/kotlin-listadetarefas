package com.example.desafiopraticolistadetarefas.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.desafiopraticolistadetarefas.data.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAll(): LiveData<List<Task>>

    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM tasks")
    suspend fun clearAll()
}
