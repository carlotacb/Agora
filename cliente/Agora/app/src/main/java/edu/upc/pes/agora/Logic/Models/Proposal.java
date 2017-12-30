package edu.upc.pes.agora.Logic.Models;

import com.google.android.gms.maps.model.LatLng;

public class Proposal {

    // Basic proposals data manipulation class
    // Contains basic information of the proposals

    private int id;
    private String title;
    private String description;
    private String owner;
    private String categoria;
    //private int token_creator;
    private LatLng position;

    public Proposal(int id, String tit, String des, String ow, String ca) {
        this.id = id;
        title = tit;
        description = des;
        owner = ow;
        categoria = ca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String director) {
        this.description = director;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    /*public int getToken_creator() {
            return token_creator;
        }

        public void setToken_creator(int token_creator) {
            this.token_creator = token_creator;
        }
    */
    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        //return String.format("%s - %s", title, description);
        return String.format("%s", title);
    }


}