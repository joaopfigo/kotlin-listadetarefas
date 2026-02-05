package com.example.desafiopraticolistadetarefas.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.desafiopraticolistadetarefas.data.database.TaskDatabase
import com.example.desafiopraticolistadetarefas.data.model.Task
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = TaskDatabase.getInstance(app).taskDao()

    private val _mensagem = MutableLiveData<String?>(null)
    val mensagem: LiveData<String?> = _mensagem

    val tasks = dao.getAll()

    fun add(task: Task) = viewModelScope.launch {
        runCatching { dao.insert(task) }
            .onFailure { _mensagem.postValue("Nao foi possivel salvar a tarefa") }
    }

    fun update(task: Task) = viewModelScope.launch {
        runCatching { dao.update(task) }
            .onFailure { _mensagem.postValue("Nao foi possivel atualizar a tarefa") }
    }

    fun delete(task: Task) = viewModelScope.launch {
        runCatching { dao.delete(task) }
            .onFailure { _mensagem.postValue("Nao foi possivel excluir a tarefa") }
    }

    fun clearAll() = viewModelScope.launch {
        runCatching { dao.clearAll() }
            .onFailure { _mensagem.postValue("Nao foi possivel limpar as tarefas") }
    }

    fun limparMensagem() {
        _mensagem.value = null
    }
}
