package com.example.projetofinal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class EsqueciSenha extends AppCompatActivity {

    private EditText email;
    private AppCompatButton restore;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_esqueci_senha);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        email = findViewById(R.id.email);
        restore = findViewById(R.id.restore);
        pb = findViewById(R.id.progressBar2);
    }

    public void restorePass(View v) {
        String userEmail = email.getText().toString();

        if (userEmail.isEmpty()) {
            Snackbar snac = Snackbar.make(
                    v, "Por favor, insira um e-mail", Snackbar.LENGTH_SHORT);
            snac.setBackgroundTint(Color.WHITE);
            snac.setTextColor(Color.BLACK);
            snac.show();
        } else {
            sendRestoreRequest(v);
        }
    }

    private void sendRestoreRequest(View v) {
        String userEmail = email.getText().toString();

        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Snackbar snac = Snackbar.make(
                                    v, "E-mail de recuperação enviado!", Snackbar.LENGTH_SHORT);
                            snac.setBackgroundTint(Color.WHITE);
                            snac.setTextColor(Color.BLACK);
                            snac.show();

                            pb.setVisibility(View.INVISIBLE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    backToStart();
                                }
                            }, 1500);
                        } else {
                            String erro;
                            try {
                                throw task.getException();
                            } catch (Exception e) {
                                erro = "E-mail inválido ou não cadastrado";
                            }
                            Snackbar snac = Snackbar.make(
                                    v, erro, Snackbar.LENGTH_SHORT);
                            snac.setBackgroundTint(Color.WHITE);
                            snac.setTextColor(Color.BLACK);
                            snac.show();
                        }
                    }
                });
    }

    public void backToStart() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
    }
}