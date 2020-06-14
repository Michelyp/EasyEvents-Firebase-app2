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

public class OptionActivity extends AppCompatActivity {
    private Evento p, p1;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, databaseReference2, databaseReference3;
    private Button crear;
    private Button vermiev;
    private Button invitado1;
    private EditText e1;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        crear = (Button) findViewById(R.id.botonCrear);
        vermiev = (Button) findViewById(R.id.botonVermiev);
        invitado1 = (Button) findViewById(R.id.botonSoyInvitado);
        e1 = (EditText) findViewById(R.id.editTextidevent);


        database = FirebaseDatabase.getInstance();
        //referencia el activiy de crear evento
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent crear = new Intent(OptionActivity.this, CrearEvento.class);

                startActivity(crear);
            }
        });
    //mira sus eventos creados
        vermiev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vermievnto = new Intent(OptionActivity.this, EventoDetalle.class);
                startActivity(vermievnto);

            }
        });

    // pone el id del evento al que esta invitado y se redirige
        invitado1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidData()) {

                    final String bs = e1.getText().toString();
                    String userid = firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Eventos");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataevt : dataSnapshot.getChildren()) {
                                for (DataSnapshot eventv : dataevt.getChildren()) {
                                    p = eventv.getValue(Evento.class);
                                    Intent eventinvi = new Intent(OptionActivity.this, EventoInvitado.class);
                                    eventinvi.putExtra("idevent", bs);
                                    startActivity(eventinvi);
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(OptionActivity.this, "Ingrese datos, porfavor", Toast.LENGTH_LONG).show();

                }

            }


        });

    }

    //valida los datos del edit text buscar
    private boolean isValidData() {

        if (e1.getText().toString().length() > 0

        ) {
            return true;
        } else {
            return false;
        }
    }
}