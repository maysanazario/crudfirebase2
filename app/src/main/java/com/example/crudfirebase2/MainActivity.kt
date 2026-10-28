
package com.example.crudfirebase2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

private val db by lazy { Firebase.firestore }

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val edtNome = findViewById<EditText>(R.id.edtNome)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtCargo = findViewById<EditText>(R.id.edtCargo)
        val btnCad = findViewById<Button>(R.id.btnCadastrar)

        btnCad.setOnClickListener {
            val nome = edtNome.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val cargo = edtCargo.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty() || cargo.isEmpty()) {
                Toast.makeText(
                    this,
                    "Preencha todos os campos!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                cadastrarFuncionario(nome, email, cargo) {
                    edtNome.text.clear()
                    edtEmail.text.clear()
                    edtCargo.text.clear()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    private fun cadastrarFuncionario(
        nome: String,
        email: String,
        cargo: String,
        onSucesso: () -> Unit
    ) {
        val funcionario = hashMapOf(
            "nome" to nome,
            "email" to email,
            "cargo" to cargo
        )

        db.collection("funcionarios")
            .add(funcionario)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Funcionário cadastrado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()

                onSucesso()
            }
            .addOnFailureListener { erro ->
                Toast.makeText(
                    this,
                    "Erro ao cadastrar: ${erro.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}
