package edu.upc.pes.agora.Logic;

/**
 * Created by carlo on 05/11/2017.
 */

public class Proposals {

    // Basic proposals data manipulation class
    // Contains basic information of the proposals

    //private long id;
    private String title;
    private String description;
    private int token_creator;

    /*public long getId() {
        return id;
    }*/

    /*public void setId(long id) {
        this.id = id;
    }*/

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

    public int getToken_creator() {
        return token_creator;
    }

    public void setToken_creator(int token_creator) {
        this.token_creator = token_creator;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        //return String.format("%s - %s", title, description);
        return String.format("%s", title);
    }

}
