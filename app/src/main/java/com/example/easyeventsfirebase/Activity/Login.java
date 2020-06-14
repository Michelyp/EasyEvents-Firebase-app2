package com.example.easyeventsfirebase.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyeventsfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private Button login;
    private Button registro;
    private EditText emailInicial;
    private TextView recuperarContra;
    private EditText passwordInicial;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        References();
        //no deja loguearte si no tienes bien los campos
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = emailInicial.getText().toString();
                String password = passwordInicial.getText().toString();

                if (mail.isEmpty() || password.isEmpty()) {
                    showMessage("Porfavor. Verifique los campos");
                } else {
                    signIn(mail, password);
                }
            }
        });
        // si no tenemos una cuenta nos regitramos
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loggy = new Intent(Login.this, Registrar.class);
                startActivity(loggy);
                finish();

            }
        });

        // recuperar la contraseña
        recuperarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RecuperarcontraActivity.class);
                startActivity(intent);
            }
        });
    }

    //método para corroborar que el usuario se encuentre en la base de datos
    private void signIn(String mail, String password) {


        mAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    updateUI();

                } else {
                    showMessage(task.getException().getMessage());

                }


            }
        });
    }

    //variables declaradas con sus respectivos nombres e id
    private void References() {
        login = (Button) findViewById(R.id.botonLogin);
        registro = (Button) findViewById(R.id.botonRegistro);
        emailInicial = (EditText) findViewById(R.id.MailInicial);
        passwordInicial = (EditText) findViewById(R.id.PasswordInicial);
        recuperarContra = (TextView) findViewById(R.id.RecuperarContraseña);

    }

    //verifica que los datos no esten vacíos
    private boolean isValidData() {
        if (emailInicial.getText().toString().length() > 0 &&
                passwordInicial.getText().toString().length() > 0
        ) {
            return true;
        } else {
            return false;
        }
    }

    //verifica que sea un email válido
    private boolean validar() {
        String correo = emailInicial.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingresa un email válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    private void updateUI() {
        Intent loggy = new Intent(Login.this, OptionActivity.class);
        startActivity(loggy);
        finish();
    }

}
