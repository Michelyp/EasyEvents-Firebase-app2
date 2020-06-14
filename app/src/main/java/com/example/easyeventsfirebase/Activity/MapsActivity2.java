package com.example.easyeventsfirebase.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.easyeventsfirebase.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class MapsActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap map;
    SearchView searchView;
    SupportMapFragment mapFragment;
    FloatingActionButton mapmas;
    private Address address;
    KeyEvent event;
    int keyCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //flecha volver atras mapa

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = findViewById(R.id.buscar);
        mapmas = findViewById(R.id.add_map);
        mapmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity2.this, EventoCreando.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("address", address);
                startActivity(intent);
                finish();
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            Bundle parametros = new Bundle();
            String datos = parametros.getString("datos");


            @Override
            public boolean onQueryTextSubmit(String s) {
                //recoge los datos introducido en el buscador
                String locacion = searchView.getQuery().toString();
                //lista de los lugares
                List<Address> addressList = null;

                //verifica si hay datos introducidos
                if (locacion != null || !locacion.equals("")) {
                    Geocoder geocoder = new Geocoder(MapsActivity2.this);
                    try {
                        addressList = geocoder.getFromLocationName(locacion, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addressList.size() > 0) {
                        address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLng).title(locacion));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    }

                    searchView.setOnKeyListener(new View.OnKeyListener() {
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            // If the event is a key-down event on the "enter" button
                            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                // Perform action on key press
                                searchView.setFocusable(false);
                                return true;
                            }
                            return false;
                        }
                    });


                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);


    }

    //Llama al googlemap
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

    }

    //Devuelve la variableen el editText, insertada en el buscador
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(MapsActivity2.this, EventoCreando.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("address", address);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(MapsActivity2.this, EventoCreando.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("address", address);
        startActivity(intent);
        finish();

    }

}