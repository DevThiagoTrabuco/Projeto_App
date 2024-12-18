package com.example.projetofinal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Cadastro extends AppCompatActivity {

    private EditText nmPacient, bdayPacient, cpfPacient, nmRelative, cpfRelative, emailRelative, phoneRelative;
    private AppCompatButton register, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nmPacient = findViewById(R.id.namePacient);
        cpfPacient= findViewById(R.id.cpfPacient);
        bdayPacient = findViewById(R.id.bdayPacient);
        nmRelative = findViewById(R.id.nameRelative);
        cpfRelative = findViewById(R.id.cpfRelative);
        phoneRelative = findViewById(R.id.phoneRelative);
        emailRelative = findViewById(R.id.emailRelative);
        register = findViewById(R.id.registerBttn);
    }

    public void registerPacient(View v){
        String nome = nmPacient.getText().toString();

        if(!validadeFields()){
            Snackbar.make(v, "Por favor, preencha todos os campos", Snackbar.LENGTH_SHORT).show();
        } else {
            registerData(v);
            Intent i = new Intent(getApplicationContext(), EditarPaciente.class);
            i.putExtra("nome", nome);
            startActivity(i);
        }
    }

    private boolean validadeFields() {
        EditText[] campos = {nmPacient, cpfPacient, bdayPacient, nmRelative, cpfRelative, phoneRelative, emailRelative};
        boolean todosPreenchidos = true;

        for (EditText campo : campos) {
            if (campo.getText().toString().isEmpty()) {
                campo.setError("Este campo é obrigatório.");
                todosPreenchidos = false;
            }
        }
        return todosPreenchidos;
    }

    private void registerData(View v) {
        String nomePaciente = nmPacient.getText().toString();
        String cpfPaciente = cpfPacient.getText().toString();
        String aniversarioPaciente = bdayPacient.getText().toString();
        String nomeResponsavel = nmRelative.getText().toString();
        String cpfResponsavel = cpfRelative.getText().toString();
        String telefoneResponsavel = phoneRelative.getText().toString();
        String emailResponsavel = emailRelative.getText().toString();
        
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        Map<String, Object> dadosPacientes = new HashMap<>();
        dadosPacientes.put("Nome_paciente: ", nomePaciente);
        dadosPacientes.put("Cpf_paciente: ", cpfPaciente);
        dadosPacientes.put("Aniversário_paciente: ", aniversarioPaciente);
        dadosPacientes.put("Nome_responsável: ", nomeResponsavel);
        dadosPacientes.put("Cpf_responsável: ", cpfResponsavel);
        dadosPacientes.put("Telefone_responsável: ", telefoneResponsavel);
        dadosPacientes.put("Email_responsável: ", emailResponsavel);

        DocumentReference dr = db.collection("Pacientes").document(nomePaciente);
        dr.set(dadosPacientes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Snackbar snac = Snackbar.make
                            (v, "Cadastro concluído", Snackbar.LENGTH_SHORT);
                    snac.show();
                } else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        erro = "Erro ao cadastrar dados";
                    }
                    Snackbar snac = Snackbar.make
                            (v, erro, Snackbar.LENGTH_SHORT);
                    snac.show();
                }
            }
        });
    }

    public void backToMenu(View v) {
        Intent i = new Intent(getApplicationContext(), MenuPrincipalFuncionario.class);
        startActivity(i);
        finish();
    }
}