package com.example.easyeventsfirebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.easyeventsfirebase.Models.Evento;
import com.example.easyeventsfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Inicio extends AppCompatActivity {
    private Evento p, p1;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, databaseReference2, databaseReference3;
    private Button iniciaregistra;
    private Button invitado;
    private EditText buscar;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        iniciaregistra = (Button) findViewById(R.id.botonregisinicia);
        invitado = (Button) findViewById(R.id.botonInvitado);
        buscar = (EditText) findViewById(R.id.editTextCodigoEvento);
        //declaramos a que actividad nos lleva el boton
        iniciaregistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ir = new Intent(Inicio.this, Login.class);
                startActivity(ir);
                finish();

            }
        });
        //declaramos a que actividad nos lleva el boton
        invitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidData()) {

                    final String bs = buscar.getText().toString();
                    String userid = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Eventos");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataevt : dataSnapshot.getChildren()) {
                                for (DataSnapshot eventv : dataevt.getChildren()) {
                                    p = eventv.getValue(Evento.class);
                                    Intent eventinvi = new Intent(Inicio.this, InviInicio.class);
                                    eventinvi.putExtra("idevent", bs);
                                    startActivity(eventinvi);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            //evita que los campos esten vacios
                } else {
                    Toast.makeText(Inicio.this, "Ingrese datos, porfavor", Toast.LENGTH_LONG).show();

                }

            }

        });


    }

    //valida los datos introducidos
    private boolean isValidData() {
        if (buscar.getText().toString().length() > 0

        ) {
            return true;
        } else {
            return false;
        }
    }
}
