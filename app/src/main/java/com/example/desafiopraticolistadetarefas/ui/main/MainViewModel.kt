package com.example.desafiopraticolistadetarefas.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafiopraticolistadetarefas.data.database.TaskDatabase
import com.example.desafiopraticolistadetarefas.data.model.Task
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = TaskDatabase.getInstance(app).taskDao()

    val tasks = dao.getAll()

    fun add(task: Task) = viewModelScope.launch { dao.insert(task) }
    fun update(task: Task) = viewModelScope.launch { dao.update(task) }
    fun delete(task: Task) = viewModelScope.launch { dao.delete(task) }
}
