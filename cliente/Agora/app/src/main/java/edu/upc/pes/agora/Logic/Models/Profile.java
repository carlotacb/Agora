package edu.upc.pes.agora.Logic.Models;

public class Profile {

    // Basic proposals data manipulation class
    // Contains basic information of the proposals

    private String name;
    private String username;
    private Integer CP;
    private String neighborhood;
    private String born;
    private String description;
    private String sex;

    public int getSex() {
        int sexo = 0;
        if(sex==null)return sexo;
        if(sex.equals("I")) sexo = 0;
        if(sex.equals("M")) sexo = 2;
        if(sex.equals("F")) sexo = 1;

        return sexo;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

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
        this.username = username;
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

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String director) {
        this.description = director;
    }
}
