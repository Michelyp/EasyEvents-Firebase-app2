package com.example.easyeventsfirebase.Models;

public class Evento {
    private String image;
    private String mensaje;
    private String description;
    private String nameper;
    private String fecha;
    private String hora;
    private String lugar;
    private String idevent;

    public Evento() {

    }

    //declaramos las variables de nuestro adaptador
    public Evento(String link, String mensaje, String description, String nameper, String fecha, String hora, String lugar, String idevent) {
        this.image = link;
        this.mensaje = mensaje;
        this.description = description;
        this.nameper = nameper;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.idevent = idevent;

    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameper() {
        return nameper;
    }

    public void setNameper(String nameper) {
        this.nameper = nameper;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getIdevent() {
        return idevent;
    }

    public void setIdevent(String idevent) {
        this.idevent = idevent;
    }
}


