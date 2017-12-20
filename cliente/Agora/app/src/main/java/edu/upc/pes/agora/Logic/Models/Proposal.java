package edu.upc.pes.agora.Logic.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;


public class Proposal implements Parcelable{


    // Basic proposals data manipulation class
    // Contains basic information of the proposals

    private int id;
    private String title;
    private String description;
    private String owner;
    private String categoria;
    //private int token_creator;
    private LatLng position;
    private double lat;
    private double lng;


    public Proposal (int id, String tit, String des, String ow, String ca, double lat, double lng) {
        this.id = id;
        title = tit;
        description = des;
        owner = ow;
        categoria = ca;
        this.lat = lat;
        this.lng = lng;
    }
    public Proposal (int id, String tit, String des, String ow, String ca) {
         this.id = id;
        title = tit;
        description = des;
        owner = ow;
        categoria = ca;
        this.lat = 0;
        this.lng = 0;
    }

    protected Proposal(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        owner = in.readString();
        categoria = in.readString();
        position = in.readParcelable(LatLng.class.getClassLoader());
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<Proposal> CREATOR = new Creator<Proposal>() {
        @Override
        public Proposal createFromParcel(Parcel in) {
            return new Proposal(in);
        }

        @Override
        public Proposal[] newArray(int size) {
            return new Proposal[size];
        }
    };

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(description);
        parcel.writeString(owner);
        parcel.writeString(categoria);
        parcel.writeParcelable(position, i);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }
}
