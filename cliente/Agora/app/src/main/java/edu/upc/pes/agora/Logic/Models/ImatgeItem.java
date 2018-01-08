package edu.upc.pes.agora.Logic.Models;


import android.graphics.Bitmap;

public class ImatgeItem {

    private String imatge;
    private int numero;
    private int idproposta;

    public int getIdproposta() {
        return idproposta;
    }

    public void setIdproposta(int idproposta) {
        this.idproposta = idproposta;
    }

    public String getImatge() {
        return imatge;
    }

    public void setImatge(String imatge) {
        this.imatge = imatge;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }
}
