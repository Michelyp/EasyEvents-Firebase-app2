package com.example.easyeventsfirebase.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.easyeventsfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Registrar extends AppCompatActivity {
    private Button IniciarSesion;
    private Button registro;
    CheckBox check;
    TextView priv;
    private FirebaseAuth mAuth;
    private EditText nombre, Telefono, mailInicial, passwordInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        mAuth = FirebaseAuth.getInstance();
        References();

        SpannableString miTexto = new SpannableString("Politica de Privacidad");
        miTexto.setSpan(new UnderlineSpan(), 0, miTexto.length(), 0);
        priv.setText(miTexto);


        IniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IniciarSesion = new Intent(Registrar.this, Login.class);
                startActivity(IniciarSesion);
            }
        });

        //te muestra todos los datos a introducir para poder registrate
        registro.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = mailInicial.getText().toString();
                String password = passwordInicial.getText().toString();
                String name = nombre.getText().toString();
                int phone = Integer.parseInt(Telefono.getText().toString());
                CheckBox check;
                check = (CheckBox) findViewById(R.id.chek);
                Intent loggy = new Intent(Registrar.this, Login.class);
                if (email.isEmpty() || name.isEmpty() || password.isEmpty()) {

                    // si algo sale mal: todos los campos deben llenarse
                    // necesitamos mostrar un mensaje de error
                    showMessage("Por favor. Verifica tus datos");

                } else {
                    //todo está bien y todos los campos están llenos ahora podemos comenzar a crear una cuenta de usuario
                    // El método CreateUserAccount intentará crear el usuario si el correo electrónico es válido
                    showMessage("Te has registrado correctamente, bienvenido");
                    CreateUserAccount(name, phone, email, password);
                }

            }


        });

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Toast.makeText(Registrar.this, "Has aceptado nuestra política de privacidad", Toast.LENGTH_SHORT).show();
                }
            }
        });

        priv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registrar.this, Privacidad.class));


            }
        });
    }

    private void CreateUserAccount(final String nombre, final int phone, final String email, final String password) {


        //este método crea una cuenta de usuario con correo electrónico y contraseña específicos
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Nos aseguramos de que la contraseña tenga 6 caracteres
                if (password.length() < 6) {
                    Toast.makeText(Registrar.this, "Debe introducir un password de al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Registrar.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // el usuario creo la cuenta correctamente
                                showMessage("Se creo la cuenta");
                                // after we created user account we need to update his profile picture and name
                                updateUserInfo(nombre, phone, email, password, RootRef);
                            } else {

                                // account creation failed
                                showMessage("account creation failed" + task.getException().getMessage());


                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // actualiza el nombre del usuario
    private void updateUserInfo(String nombre, int phone, String email, String password, DatabaseReference rootRef) {
        final String ID = mAuth.getCurrentUser().getUid();

        HashMap<String, Object> userdataMap = new HashMap<>();
        userdataMap.put("ID", ID);
        userdataMap.put("nombre", nombre);
        userdataMap.put("phone", phone);
        userdataMap.put("email", email);
        userdataMap.put("password", password);

        rootRef.child("Usuarios").child(ID).updateChildren(userdataMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) { //En caso exitoso
                            showMessage("Datos guardados");
                            updateUI();

                        } else { //En caso de error
                            showMessage("Error en guardar los datos");
                        }
                    }
                });

        mAuth.signOut();

    }

    private void References() {
        nombre = (EditText) findViewById(R.id.Nombre);
        Telefono = (EditText) findViewById(R.id.telefono);
        mailInicial = (EditText) findViewById(R.id.MailInicial);
        passwordInicial = (EditText) findViewById(R.id.PasswordInicial);
        IniciarSesion = (Button) findViewById(R.id.botonIniciarSesion);
        registro = (Button) findViewById(R.id.botonCrearCuenta);
        check = (CheckBox) findViewById(R.id.chek);
        priv = (TextView) findViewById(R.id.privacidad);
    }

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void updateUI() {
        Intent mainActivity = new Intent(getApplicationContext(), Login.class);
        startActivity(mainActivity);
    }
}