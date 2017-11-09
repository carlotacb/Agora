package edu.upc.pes.agora.Logic;

import java.util.Date;

/**
 * Created by carlo on 07/11/2017.
 */

public class Profile {

    // Basic proposals data manipulation class
    // Contains basic information of the proposals

    private String name;
    private String username;
    private Integer CP;
    private String neighborhood;
    private Date born;
    private String description;
    //private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.description = username;
    }

    public Integer getCP() {
        return CP;
    }

    public void setCP(Integer CP) {
        this.CP = CP;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }
    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String director) {
        this.description = director;
    }

    // Will be used by the ArrayAdapter in the ListView
    /*@Override
    public String toString() {
        //return String.format("%s - %s", title, description);
        return String.format("%s", title);
    }*/

}
