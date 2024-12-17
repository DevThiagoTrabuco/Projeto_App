package com.example.projetofinal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainActivity extends AppCompatActivity {

    private EditText login, pass;
    private AppCompatButton signIn;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        login = findViewById(R.id.login);
        pass = findViewById(R.id.pass);
        signIn = findViewById(R.id.signIn);
        pb = findViewById(R.id.progressBar);

        FirebaseAuth.getInstance().
                createUserWithEmailAndPassword("arkham.asylum@mail.com", "im_batman");
    }
    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            signIn();
        }
    }

    public void logIn(View v){
        String user = login.getText().toString();
        String passwrd = pass.getText().toString();

        if(user.isEmpty() || passwrd.isEmpty()){
            Snackbar snac = Snackbar.make
                    (v, "Preencha todos os campos", Snackbar.LENGTH_SHORT);
            snac.setBackgroundTint(Color.WHITE);
            snac.setTextColor(Color.BLACK);
            snac.show();
        } else {
            signInUser(v);
        }
    }

    private void signInUser(View v) {
        String user = login.getText().toString();
        String passwrd = pass.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(user, passwrd).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            pb.setVisibility(View.VISIBLE);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    signIn();
                                }
                            }, 2500);
                        } else {
                            String erro;
                            try{
                                throw task.getException();
                            } catch(Exception e){
                                erro = "Usuário ou senha incorretos";
                            }
                            Snackbar snac = Snackbar.make
                                    (v, erro, Snackbar.LENGTH_SHORT);
                            snac.setBackgroundTint(Color.WHITE);
                            snac.setTextColor(Color.BLACK);
                            snac.show();
                        }
                    }
                });
    }

    public void signIn() {
        Intent i = new Intent(getApplicationContext(), MenuPrincipalFuncionario.class);
        startActivity(i);
    }
}
