package com.example.easyeventsfirebase.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.easyeventsfirebase.Interfaces.ItemClickListener;
import com.example.easyeventsfirebase.R;

public class EventoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtTitulo, txtMensaje, txtNombreper, txtFecha, txthora, txtDireccion, txtId;
    public ImageView ImagenFoto;
    public ItemClickListener listener;

    public EventoViewHolder(@NonNull View itemView) {
        super(itemView);
        txtTitulo = (TextView) itemView.findViewById(R.id.titleEv);
        txtMensaje = (TextView) itemView.findViewById(R.id.mensjEv);
        txtNombreper = (TextView) itemView.findViewById(R.id.nombrePer);
        txtFecha = (TextView) itemView.findViewById(R.id.FechaEv);
        txthora = (TextView) itemView.findViewById(R.id.horaEv);
        txtDireccion = (TextView) itemView.findViewById(R.id.direccEv);
        txtId = (TextView) itemView.findViewById(R.id.idEvent);
        ImagenFoto = (ImageView) itemView.findViewById(R.id.foto1);

    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}