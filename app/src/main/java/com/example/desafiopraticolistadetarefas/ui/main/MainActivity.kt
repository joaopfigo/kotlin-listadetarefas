package com.example.desafiopraticolistadetarefas.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiopraticolistadetarefas.R
import com.example.desafiopraticolistadetarefas.data.model.Task
import com.example.desafiopraticolistadetarefas.ui.task.TaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
                // EDITAR (Task é imutável -> copy)
                val antiga = tarefas[index]
                val editada = antiga.copy(titulo = titulo, descricao = descricao)
                tarefas[index] = editada
                adapter.notifyItemChanged(index)

                // Se você já estiver usando Room de verdade, aqui seria: viewModel.update(editada)
            } else {
                // NOVA
                val nova = Task(titulo = titulo, descricao = descricao)
                tarefas.add(nova)
                adapter.notifyItemInserted(tarefas.lastIndex)

                // Se você já estiver usando Room de verdade, aqui seria: viewModel.add(nova)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecyclerView + Adapter (com clique para editar)
        val rv = findViewById<RecyclerView>(R.id.rvTasks)

        adapter = TaskAdapter(tarefas) { index ->
            val t = tarefas[index]
            val intent = Intent(this, TaskActivity::class.java).apply {
                putExtra(TaskActivity.EXTRA_INDEX, index)
                putExtra(TaskActivity.EXTRA_TITULO, t.titulo)
                putExtra(TaskActivity.EXTRA_DESCRICAO, t.descricao)
            }
            taskFormLauncher.launch(intent)
        }

        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        // Swipe para excluir
        val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                if (pos < 0 || pos >= tarefas.size) return

                val removida = tarefas.removeAt(pos)
                adapter.notifyItemRemoved(pos)

                // Se você já estiver usando Room de verdade, aqui seria: viewModel.delete(removida)
            }
        }
        ItemTouchHelper(swipe).attachToRecyclerView(rv)

        // FAB: adicionar
        val fab = findViewById<FloatingActionButton>(R.id.fabAdd)
        fab.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
                .putExtra(TaskActivity.EXTRA_INDEX, -1)
            taskFormLauncher.launch(intent)
        }

        // Carregar do Room (se tiver)
        viewModel.tasks.observe(this) { lista ->
            tarefas.clear()
            tarefas.addAll(lista)
            adapter.notifyDataSetChanged()
        }
    }
}
