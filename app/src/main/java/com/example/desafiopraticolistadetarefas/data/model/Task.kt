package com.example.desafiopraticolistadetarefas.data.model

data class Task(
    val titulo: String,
    val descricao: String? = null,
    var concluida: Boolean = false
)
