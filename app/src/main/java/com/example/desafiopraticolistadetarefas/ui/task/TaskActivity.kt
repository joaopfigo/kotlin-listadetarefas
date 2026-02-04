package com.example.desafiopraticolistadetarefas.ui.task

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafiopraticolistadetarefas.R
import android.app.Activity
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_task)
        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)

        val index = intent.getIntExtra(EXTRA_INDEX, -1)
        if (index >= 0) {
            etTitulo.setText(intent.getStringExtra(EXTRA_TITULO) ?: "")
            etDescricao.setText(intent.getStringExtra(EXTRA_DESCRICAO) ?: "")
        }

        btnSalvar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val descricao = etDescricao.text.toString().trim().ifBlank { null }

            if (titulo.isBlank()) {
                Toast.makeText(this, "Título é obrigatório", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val data = Intent().apply {
                putExtra(EXTRA_INDEX, index)
                putExtra(EXTRA_TITULO, titulo)
                putExtra(EXTRA_DESCRICAO, descricao)
            }
            setResult(Activity.RESULT_OK, data)
            finish()
        }

        btnCancelar.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    companion object {
        const val EXTRA_INDEX = "EXTRA_INDEX"
        const val EXTRA_TITULO = "EXTRA_TITULO"
        const val EXTRA_DESCRICAO = "EXTRA_DESCRICAO"
    }
}