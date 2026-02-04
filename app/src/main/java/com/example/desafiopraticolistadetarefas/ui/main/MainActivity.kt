package com.example.desafiopraticolistadetarefas.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.desafiopraticolistadetarefas.R
import com.example.desafiopraticolistadetarefas.data.model.Task
import com.example.desafiopraticolistadetarefas.ui.task.TaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val tarefas = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    private val taskFormLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult

            val data = result.data ?: return@registerForActivityResult

            val index = data.getIntExtra(TaskActivity.EXTRA_INDEX, -1)
            val titulo = data.getStringExtra(TaskActivity.EXTRA_TITULO) ?: return@registerForActivityResult
            val descricao = data.getStringExtra(TaskActivity.EXTRA_DESCRICAO)

            if (index >= 0 && index < tarefas.size) {
                // EDITAR (usa copy porque Task tem val)
                tarefas[index] = tarefas[index].copy(titulo = titulo, descricao = descricao)
                adapter.notifyItemChanged(index)
            } else {
                // NOVA
                tarefas.add(Task(titulo = titulo, descricao = descricao))
                adapter.notifyItemInserted(tarefas.lastIndex)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tarefas.addAll(
            listOf(
                Task("Estudar Kotlin", "val/var, funções, null safety"),
                Task("Arrumar layout", "ConstraintLayout sem absoluteX/Y"),
                Task("Implementar salvar", "Retornar dados da TaskActivity")
            )
        )

        // RecyclerView
        val rv = findViewById<RecyclerView>(R.id.rvTasks)
        adapter = TaskAdapter(tarefas)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                tarefas.removeAt(pos)
                adapter.notifyItemRemoved(pos)
            }
        }

        ItemTouchHelper(swipe).attachToRecyclerView(rv)

        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)
        fab.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
                .putExtra(TaskActivity.EXTRA_INDEX, -1)
            taskFormLauncher.launch(intent)
        }

        viewModel.tasks.observe(this) { lista ->
            tarefas.clear()
            tarefas.addAll(lista)
            adapter.notifyDataSetChanged()
        }
    }
}
