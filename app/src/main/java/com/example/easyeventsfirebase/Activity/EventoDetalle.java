package com.example.easyeventsfirebase.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.easyeventsfirebase.Adapter.AdapterLista;
import com.example.easyeventsfirebase.Models.Evento;
import com.example.easyeventsfirebase.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventoDetalle extends AppCompatActivity {
    private static final String TAG = "EventoDetalle";

    public AdapterLista adapterList;
    public List<Evento> eventos;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private FloatingActionButton fab;

    static String eventa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_detalle);
        getSupportActionBar().setTitle("Mis Eventos");

        inicializarFirebase();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();
        eventos = new ArrayList<Evento>();

        recyclerView = (RecyclerView) findViewById(R.id.lista);
        mLayoutManager = new LinearLayoutManager(this);
        onStart();
        //boton flotante para crear un nuevo evento
        fab = (FloatingActionButton) findViewById(R.id.add_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventa = "";
                Intent crear = new Intent(EventoDetalle.this, EventoCreando.class);


                startActivity(crear);
            }
        });

        //referencia en la base de datos para crear los eventos por el id del usuario
        databaseReference.child("Eventos").child(userid).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Evento p = dataSnapshot1.getValue(Evento.class);
                    //p.setId(dataSnapshot1.getKey());
                    eventos.add(p);

                }
                //mostramos la lista de los eventos creados por el usuario
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(adapterList);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final DatabaseReference EventoRef = FirebaseDatabase.getInstance().getReference().child("Eventos").child(userid);

        FirebaseRecyclerOptions<Evento> opciones = new FirebaseRecyclerOptions.Builder<Evento>()
                .setQuery(EventoRef.orderByChild("nombre").startAt(eventa), Evento.class)
                .build();

        adapterList = new AdapterLista(eventos, R.layout.itemlista, this,
                new AdapterLista.OnItemClickListener() {

                    @Override
                    public void onItemClick(@NonNull final Evento evento, final int position) {
                        adapterList.notifyItemChanged(position);


                    }
                });
        adapterList.notifyDataSetChanged();

    }


    private void inicializarFirebase() {
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }


}
