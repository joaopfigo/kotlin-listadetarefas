package com.example.desafiopraticolistadetarefas.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiopraticolistadetarefas.R
import com.example.desafiopraticolistadetarefas.data.model.Task

class TaskAdapter(
    private val itens: List<Task>
) : RecyclerView.Adapter<TaskAdapter.ItemTarefaViewHolder>() {

    class ItemTarefaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkStatus: CheckBox = itemView.findViewById(R.id.status)
        val textoTitulo: TextView = itemView.findViewById(R.id.titulo)
        val textoDescricao: TextView = itemView.findViewById(R.id.descricao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTarefaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ItemTarefaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemTarefaViewHolder, position: Int) {
        val tarefa = itens[position]
        holder.textoTitulo.text = tarefa.titulo
        holder.textoDescricao.text = tarefa.descricao ?: ""
        holder.checkStatus.isChecked = tarefa.concluida
    }

    override fun getItemCount(): Int = itens.size
}
