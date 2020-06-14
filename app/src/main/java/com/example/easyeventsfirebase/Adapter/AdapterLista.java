package com.example.easyeventsfirebase.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

import com.example.easyeventsfirebase.Activity.EventoCreando;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterLista extends RecyclerView.Adapter<AdapterLista.ViewHolder> {
    private FirebaseAuth auth;
    //public static String miid;
    public static String titulo;
    public static String descripcion;
    public static String nombreper;
    public static String fecha;
    public static String hora;
    public static String lugar;
    public static String foto;
    private Context mContext;
    private List<Evento> eventos;
    private int layout;
    private Evento evento;

    private Activity activity;
    private OnItemClickListener listener;

    public AdapterLista(List<Evento> eventos, int layout, Activity activity, OnItemClickListener listener) {
        this.eventos = eventos;
        this.layout = layout;
        this.activity = activity;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        mContext = parent.getContext();
        ViewHolder vh = new ViewHolder(v);


        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(eventos.get(position), listener);
        String miid = eventos.get(position).getIdevent();
        //Toast.makeText(mContext, miid, Toast.LENGTH_SHORT).show();


    }

    @Override
    public int getItemCount() {
        return eventos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public ImageView fot1;
        public TextView t1, t2, t3, t4, t5, t6, t7;

        public ViewHolder(View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.titleEv);
            t2 = (TextView) itemView.findViewById(R.id.mensjEv);
            t3 = (TextView) itemView.findViewById(R.id.nombrePer);
            t4 = (TextView) itemView.findViewById(R.id.FechaEv);
            t5 = (TextView) itemView.findViewById(R.id.horaEv);
            t6 = (TextView) itemView.findViewById(R.id.direccEv);
            t7 = (TextView) itemView.findViewById(R.id.idEvent);
            fot1 = (ImageView) itemView.findViewById(R.id.foto1);

            itemView.setOnCreateContextMenuListener(this);


        }
//muestra los datos

        public void bind(final Evento evento, final OnItemClickListener listener) {


            this.t1.setText(evento.getMensaje());
            this.t2.setText(evento.getDescription());
            this.t3.setText(evento.getNameper());
            this.t4.setText(evento.getFecha());
            this.t5.setText(evento.getHora());
            this.t6.setText(evento.getLugar());
            this.t7.setText(evento.getIdevent());
            //Si se hace con Picasso
            Picasso.get().load(evento.getImage()).fit().into(fot1);


            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    listener.onItemClick(evento, getAdapterPosition());
                    int currentPosition = getClickedPosition(view);
                    String miid = eventos.get(currentPosition).getIdevent();

                }
            });


        }


        @Override

        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            Evento event = eventos.get(this.getAdapterPosition());
            contextMenu.setHeaderTitle(event.getMensaje());
            //contextMenu.setHeaderIcon(event.getImage());
            MenuInflater inflater = activity.getMenuInflater();

            inflater.inflate(R.menu.context_menu, contextMenu);

            for (int i = 0; i < contextMenu.size(); i++)
                contextMenu.getItem(i).setOnMenuItemClickListener(this);

        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            auth = FirebaseAuth.getInstance();
            int currentPosition = getAdapterPosition();
            final String miid = eventos.get(currentPosition).getIdevent();
            //Toast.makeText(mContext, miid, Toast.LENGTH_SHORT).show();

            //elimina datos de la lsita y bbdd
            switch (menuItem.getItemId()) {

                case R.id.delete_evento:

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                    builder1.setTitle("Alertas");
                    builder1.setMessage("¿Seguro que quieres eliminar el evento de la lista y de la BBDD?");
                    // LayoutInflater inflater = mContext.getLayoutInflater();


                    // Botones de aceptar/cancelar
                    builder1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Observa que como estamos dentro del adaptador, podemos acceder
                            Evento delevento = eventos.get(getAdapterPosition());
                            Evento deleventoid = eventos.get(getAdapterPosition());

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            final String userid = firebaseUser.getUid();


                            final DatabaseReference referencia = FirebaseDatabase.getInstance().getReference().child("Eventos");

                            referencia.child(userid).child("idevent")
                                    .setValue(null)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(mContext, "Evento eliminado de la lista", Toast.LENGTH_SHORT).show();
                                                eventos.remove(getAdapterPosition());
                                                notifyItemRemoved(getAdapterPosition());


                                            }
                                        }
                                    });

                            referencia.child(userid).orderByChild("idevent").equalTo(miid).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    referencia.child(userid).child(dataSnapshot.getKey()).setValue(null);
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

                        }
                    });

                    builder1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder1.show();

                    return true;

                case R.id.update:
                    //currentPosition = getAdapterPosition();
                    // miid= eventos.get(currentPosition).getIdevent();
                    String descripcion = eventos.get(currentPosition).getDescription();
                    String titulo = eventos.get(currentPosition).getMensaje();
                    String nombreper = eventos.get(currentPosition).getNameper();
                    String fecha = eventos.get(currentPosition).getFecha();
                    String hora = eventos.get(currentPosition).getHora();
                    String lugar = eventos.get(currentPosition).getLugar();
                    String foto = eventos.get(currentPosition).getImage();

                    Toast.makeText(mContext, "has clickeado " + miid, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, EventoCreando.class);
                    intent.putExtra("Foto", foto);
                    intent.putExtra("Titulo", titulo);
                    intent.putExtra("Descripcion", descripcion);
                    intent.putExtra("NombreP", nombreper);
                    intent.putExtra("Fecha", fecha);
                    intent.putExtra("Hora", hora);
                    intent.putExtra("Lugar", lugar);
                    intent.putExtra("IDEvent", miid);
                    // intent.putExtra("IDevento", evento.getID());
                    mContext.startActivity(intent);
                    return true;
                default:
                    return false;
            }
        }

    }


    public interface OnItemClickListener {
        void onItemClick(Evento evento, int position);
    }

    private int getClickedPosition(View clickedView) {
        RecyclerView recyclerView = (RecyclerView) clickedView.getParent();
        ViewHolder currentViewHolder = (ViewHolder) recyclerView.getChildViewHolder(clickedView);
        return currentViewHolder.getAdapterPosition();
    }


}
