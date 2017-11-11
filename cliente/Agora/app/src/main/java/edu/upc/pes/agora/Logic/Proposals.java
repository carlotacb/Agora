package edu.upc.pes.agora.Logic;

public class Proposals {

    // Basic proposals data manipulation class
    // Contains basic information of the proposals

    private int id;
    private String title;
    private String description;
    private String owner;
    //private String categoria;
    //private int token_creator;

    public Proposals (int id, String tit, String des, String ow) {
        this.id = id;
        title = tit;
        description = des;
        owner = ow;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
