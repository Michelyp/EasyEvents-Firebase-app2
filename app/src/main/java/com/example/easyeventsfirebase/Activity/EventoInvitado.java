package com.example.easyeventsfirebase.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.squareup.picasso.Picasso;

public class EventoInvitado extends AppCompatActivity {

    ValueEventListener valueEventListener;
    private Evento p;
    private String idevento;
    private DatabaseReference database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    public ImageView fot1;
    public TextView t1, t2, t3, t4, t5, t6, t7;
    private String idevent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_invitado);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Evento Invitado");

        idevent = getIntent().getStringExtra("idevent");
        database = FirebaseDatabase.getInstance().getReference("Eventos");

        //declaramos las variables que nos tiene que mostrar la actividad
        t1 = (TextView) findViewById(R.id.titleEvver);
        t2 = (TextView) findViewById(R.id.mensjEvver);
        t3 = (TextView) findViewById(R.id.nombrePerver);
        t4 = (TextView) findViewById(R.id.FechaEvver);
        t5 = (TextView) findViewById(R.id.horaEvver);
        t6 = (TextView) findViewById(R.id.direccEvver);
        t7 = (TextView) findViewById(R.id.idEventver);
        fot1 = (ImageView) findViewById(R.id.foto1);
        //cogemos los datos de la BBDD por el id del evento escrito manualmente
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataevt : dataSnapshot.getChildren()) {
                    for (DataSnapshot eventv : dataevt.getChildren()) {
                        Intent verdatos = getIntent();
                        idevento = verdatos.getStringExtra("idevent");
                        p = eventv.getValue(Evento.class);
                        if (idevento.equalsIgnoreCase(p.getIdevent())) {
                            Picasso.get().load(p.getImage()).fit().into(fot1);
                            t1.setText(p.getMensaje());
                            t2.setText(p.getDescription());
                            t3.setText(p.getNameper());
                            t4.setText(p.getFecha());
                            t5.setText(p.getHora());
                            t6.setText(p.getLugar());
                            t7.setText(p.getIdevent());
                        }

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


}
