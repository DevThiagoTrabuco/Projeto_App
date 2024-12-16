package com.example.projetofinal;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuPrincipalFuncionario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal_funcionario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void telaEditar(View view) {
    }

    public void telaCadastro(View view) {
    }

    public void sair(View view) {
    }

    public void pesquisar(View view) {
    }

    public void telaEstoque(View view) {
    }

    public void telaCuidadores(View view) {
    }

    public void telaDebitos(View view) {
    }

    public void telaContrato(View view) {
    }

    public void telaFinanceiro(View view) {
    }
}