package com.example.easyeventsfirebase.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import com.example.easyeventsfirebase.Adapter.Adaptador;
import com.example.easyeventsfirebase.R;
import com.example.easyeventsfirebase.Models.TiposEventoFoto;

public class CrearEvento extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<TiposEventoFoto> listaTiposEventoFotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);
        //Lista con los diferentes tipos de eventos que tiene la aplicación
        listaTiposEventoFotos = new ArrayList<>();
        listaTiposEventoFotos.add(new TiposEventoFoto("Cumpleaños", R.drawable.feliz));
        listaTiposEventoFotos.add(new TiposEventoFoto("Bodas", R.drawable.boda));
        listaTiposEventoFotos.add(new TiposEventoFoto("Baby Shower's", R.drawable.babyshower));
        //grid donde se almacenará las imagenes
        gridView = (GridView) findViewById(R.id.gridlayout);
        Adaptador adaptador = new Adaptador(this.getApplicationContext(), listaTiposEventoFotos);
        gridView.setAdapter(adaptador);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent event = new Intent(CrearEvento.this, EventoCreando.class);
                startActivity(event);
                finish();

            }
        });

    }
}
