package com.example.easyeventsfirebase.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.easyeventsfirebase.R;

public class Privacidad extends AppCompatActivity {
    private Button acep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacidad);

        acep = (Button) findViewById(R.id.botonAcept);
        //muestra la politica de privacidad del la app
        acep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent condiciones = new Intent(Privacidad.this, Registrar.class);

                startActivity(condiciones);
            }
        });
    }
}
