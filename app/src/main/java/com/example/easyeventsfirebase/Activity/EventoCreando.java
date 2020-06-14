package com.example.easyeventsfirebase.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.easyeventsfirebase.Models.Evento;
import com.example.easyeventsfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;

public class EventoCreando extends AppCompatActivity {
    private FirebaseAuth auth;
    private Evento evento;
    private DatabaseReference referencia;
    EditText fotEvent;
    Button verImg;
    ImageView fotoPrew;
    static String sfotEvent;
    static String snombreevento;
    static String smensaevento;
    static String snombreperson;
    static String shoraevento;
    static String sfecha;
    static String slugar;
    static String sid;
    private Calendar date;
    Button fecha, bhora;
    public EditText mfecha;
    public EditText mLugar;
    Button mapa;
    Button edit;
    public EditText nombreEvento;
    public EditText idevn;
    public EditText mensaEvento;
    public EditText nombrePerson;
    private EditText horaEvento;
    private boolean isCreation;
    TimePickerDialog TPD;
    Calendar c;
    DatePickerDialog dpd;
    private Address address;
    private Button guardar;
    private String foto;
    private String titulo;
    private String descripcion;
    private String nameper;
    private String fecha2;
    private String hora;
    private String lugar;
    private String idevent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento_creando);
        auth = FirebaseAuth.getInstance();
        //nueva variables para ver la foto url
        fotEvent = (EditText) findViewById(R.id.imagenEvento);
        verImg = (Button) findViewById(R.id.verFoto);
        fotoPrew = (ImageView) findViewById(R.id.Preview);
        edit = (Button) findViewById(R.id.eventEdit);


        // devulve la direccion buscada
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            address = (Address) bundle.get("address");
            foto = (String) bundle.get("Foto");
            titulo = (String) bundle.get("Titulo");
            descripcion = (String) bundle.get("Descripcion");
            nameper = (String) bundle.get("NombreP");
            fecha2 = (String) bundle.get("Fecha");
            hora = (String) bundle.get("Hora");
            lugar = (String) bundle.get("Lugar");
            idevent = (String) bundle.get("IDEvent");
            bundle.clear();


        }


        //Boton ver imagen
        verImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = fotEvent.getText().toString();
                if (link.length() > 0)
                    loadImageLinkForPreview(fotEvent.getText().toString());


            }


        });


        References();
        //Instancia un calendario con el día y mes
        c = Calendar.getInstance();
        final int dia = c.get(Calendar.DAY_OF_MONTH);
        final int mes = c.get(Calendar.MONTH);
        final int anno = c.get(Calendar.YEAR);
        final int hora = c.get(Calendar.HOUR_OF_DAY);
        final int minutos = c.get(Calendar.MINUTE);


        //muestra el mapa en el activitymaps
        mapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent map = new Intent(EventoCreando.this, MapsActivity2.class);
                startActivity(map);
                Bundle parametros = new Bundle();
                map.putExtras(parametros);

                sfecha = mfecha.getText().toString();
                sid = idevn.getText().toString();
                snombreevento = nombreEvento.getText().toString();
                smensaevento = mensaEvento.getText().toString();
                snombreperson = nombrePerson.getText().toString();
                shoraevento = horaEvento.getText().toString();

                //muestra la url de la imagen en el edit text
                sfotEvent = fotEvent.getText().toString();

            }
        });
        //guarda los datos introducidos para mostrar en la base datos
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidData()) {
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    String slugar = mLugar.getText().toString();
                    String sid = idevn.getText().toString();

                    String userid = firebaseUser.getUid();

                    referencia = FirebaseDatabase.getInstance().getReference().child("Eventos").child(userid);
                    Evento eventi = new Evento();
                    eventi.setImage(sfotEvent);
                    eventi.setMensaje(snombreevento);
                    eventi.setDescription(smensaevento);
                    eventi.setNameper(snombreperson);
                    eventi.setFecha(sfecha);
                    eventi.setHora(shoraevento);
                    eventi.setLugar(slugar);
                    eventi.setIdevent(sid);
                    DatabaseReference newPost = referencia.push();
                    newPost.setValue(eventi);
                    sfotEvent = "";
                    snombreevento = "";
                    smensaevento = "";
                    snombreperson = "";
                    sfecha = "";
                    shoraevento = "";

                    //se guardan los datos en el activity donde se mostrara la lista de los eventos
                    Intent ver = new Intent(EventoCreando.this, OptionActivity.class);
                    startActivity(ver);
                    finish();

                    Toast.makeText(EventoCreando.this, "Se guardaron los datos con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EventoCreando.this, "ingrese datos", Toast.LENGTH_LONG).show();

                }


            }
        });

        //muestra para seleccionar mes, día y año
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();

            }
        });

        //muestra la hora y minuto, si es am o pm
        bhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Dialogo donde se muesra la hora con los minutos
                TPD = new TimePickerDialog(EventoCreando.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        horaEvento.setText(hourOfDay + ":" + minute);
                    }
                }, hora, minutos, false);
                TPD.show();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser = auth.getCurrentUser();

                slugar = mLugar.getText().toString();
                sfecha = mfecha.getText().toString();
                snombreevento = nombreEvento.getText().toString();
                smensaevento = mensaEvento.getText().toString();
                snombreperson = nombrePerson.getText().toString();
                shoraevento = horaEvento.getText().toString();
                sfotEvent = fotEvent.getText().toString();
                final String userid = firebaseUser.getUid();
                final DatabaseReference referencia2 = FirebaseDatabase.getInstance().getReference().child("Eventos");

                referencia2.child(userid).orderByChild("idevent").equalTo(idevent).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        referencia2.child(userid).child(dataSnapshot.getKey()).child("description").setValue(smensaevento);
                        referencia2.child(userid).child(dataSnapshot.getKey()).child("fecha").setValue(sfecha);
                        referencia2.child(userid).child(dataSnapshot.getKey()).child("hora").setValue(shoraevento);
                        referencia2.child(userid).child(dataSnapshot.getKey()).child("image").setValue(sfotEvent);
                        referencia2.child(userid).child(dataSnapshot.getKey()).child("lugar").setValue(slugar);
                        referencia2.child(userid).child(dataSnapshot.getKey()).child("mensaje").setValue(snombreevento);
                        referencia2.child(userid).child(dataSnapshot.getKey()).child("nameper").setValue(snombreperson);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent ver = new Intent(EventoCreando.this, OptionActivity.class);
                startActivity(ver);
                finish();

                Toast.makeText(EventoCreando.this, "Se guardaron los nuevos  datos con éxito", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //muestra el calendario actual
    public void showDateTimePicker() {
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                mfecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                //use this date as per your requirement
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(EventoCreando.this, dateSetListener,
                currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void Limpiar() {
        fotEvent.setText("");
        nombreEvento.setText("");
        mensaEvento.setText("");
        nombrePerson.setText("");
        mfecha.setText("");
        horaEvento.setText("");
        idevn.setText("");
        mLugar.setText("");
    }
    //muestra datos

    private void bindDataToFields() {
        fotEvent.setText(evento.getImage());
        loadImageLinkForPreview(evento.getImage());
        nombreEvento.setText(evento.getMensaje());
        mensaEvento.setText(evento.getDescription());
        nombrePerson.setText(evento.getNameper());
        mfecha.setText(evento.getFecha());
        horaEvento.setText(evento.getHora());
        idevn.setText(evento.getIdevent());
        mLugar.setText(evento.getLugar());

    }


    // metodo picasso
    private void loadImageLinkForPreview(String link) {
        Picasso.get().load(link).fit().into(fotoPrew);
    }


    @Override
    protected void onResume() {

        super.onResume();


        if (address != null) {
            mLugar.setText(address.getAddressLine(0));
        }

        if (getIntent().hasExtra("Titulo")) {
            getIntent().removeExtra("Titulo");
            edit.setVisibility(View.VISIBLE);
            guardar.setVisibility(View.GONE);
            mfecha.setText(fecha2);
            mLugar.setText(lugar);
            horaEvento.setText(hora);
            idevn.setText(idevent);
            nombreEvento.setText(titulo);
            mensaEvento.setText(descripcion);
            nombrePerson.setText(nameper);
            fotEvent.setText(foto);
            idevn.setEnabled(false);
            mLugar.setEnabled(false);
            mapa.setVisibility(View.GONE);

        } else {
            guardar.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);

        }


    }


    //valida que se haya introducido los datos
    private boolean isValidData() {

        if (fotEvent.getText().toString().length() > 0 &&
                nombreEvento.getText().toString().length() > 0 &&
                mensaEvento.getText().toString().length() > 0 &&
                nombrePerson.getText().toString().length() > 0 &&
                horaEvento.getText().toString().length() > 0 &&
                idevn.getText().toString().length() > 0 &&
                mfecha.getText().toString().length() > 0

        ) {
            return true;
        } else {
            return false;
        }
    }

    //Declaración de las variables
    private void References() {
        mfecha = (EditText) findViewById(R.id.mostarfecha);
        fecha = (Button) findViewById(R.id.botonFecha);
        mLugar = (EditText) findViewById(R.id.mostarLugar);
        mapa = (Button) findViewById(R.id.irMapa);
        guardar = (Button) findViewById(R.id.eventCreado);
        nombreEvento = (EditText) findViewById(R.id.eventoName);
        idevn = (EditText) findViewById(R.id.mostarId);
        mensaEvento = (EditText) findViewById(R.id.eventoMessage);
        nombrePerson = (EditText) findViewById(R.id.personName);
        horaEvento = (EditText) findViewById(R.id.horaEvent);
        bhora = (Button) findViewById(R.id.botonHora);

        if (edit.getVisibility() == View.GONE) {
            mfecha.setText(sfecha);
            mLugar.setText(slugar);
            nombreEvento.setText(snombreevento);
            mensaEvento.setText(smensaevento);
            nombrePerson.setText(snombreperson);
            horaEvento.setText(shoraevento);
            fotEvent.setText(sfotEvent);
            idevn.setText(sid);

        }

        //declaracion de los strings

    }


}
