package com.example.projetofinal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class EditarPaciente extends AppCompatActivity {

    private TextView name;
    private EditText condition, medication, routine, obs;
    private AppCompatButton update, exit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.editar_paciente);
        String nome = getIntent().getStringExtra("nome");
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        name = findViewById(R.id.titlePacient);
        condition = findViewById(R.id.conditionPacient);
        medication = findViewById(R.id.medicationsPacient);
        routine = findViewById(R.id.routinePacient);
        obs = findViewById(R.id.obsPacient);
        update = findViewById(R.id.updateBttn);
        exit = findViewById(R.id.quitBttn);
    }

    public void onStart() {
        super.onStart();
        String nome = getIntent().getStringExtra("nome");

        if(nome != null){
            DocumentReference dr = db.collection("Pacientes").document(nome);
            dr.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error){
                    if (value != null) {
                        name.setText(value.getString("Nome_paciente"));
                    }
                }
            });
        }
    }

    public void updatePacientData(View v) {
        if(!validadeFields()){
            Snackbar.make(v, "Por favor, preencha os campos obrigatórios", Snackbar.LENGTH_SHORT).show();
        } else {
            updateData(v);
            Intent i = new Intent(getApplicationContext(), MenuPrincipalFuncionario.class);
            startActivity(i);
            finish();
        }
    }

    private boolean validadeFields() {
        EditText[] campos = {condition, medication, routine};
        boolean todosPreenchidos = true;

        for (EditText campo : campos) {
            if (campo.getText().toString().isEmpty()) {
                campo.setError("Este campo é obrigatório.");
                todosPreenchidos = false;
            }
        }
        return todosPreenchidos;
    }

    private void updateData(View v) {
        String nome = getIntent().getStringExtra("nome");

        String condicao = condition.getText().toString();
        String medicacao = medication.getText().toString();
        String rotina = routine.getText().toString();
        String observacao = obs.getText().toString();

        Map<String, Object> dadosPaciente = new HashMap<>();
        dadosPaciente.put("Condição_médica: ", condicao);
        dadosPaciente.put("Medicações: ", medicacao);
        dadosPaciente.put("Rotina: ", rotina);
        dadosPaciente.put("Observações: ", observacao);

        DocumentReference dr = db.collection("Pacientes").document(nome);
        CollectionReference cr = dr.collection("Dados");
        DocumentReference data = cr.document("Informações");

        data.set(dadosPaciente).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        Intent i = new Intent(getApplicationContext(), Cadastro.class);
        startActivity(i);
        finish();
    }
}