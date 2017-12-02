package edu.upc.pes.agora.Logic;

public class Comment {

    private String autor;
    private String identificador;
    private String comentario;
    private String created;
    private Integer identificadorProp;

    public Comment(String autor, String identificador, String comentario) {
        this.autor = autor;
        this.identificador = identificador;
        this.comentario = comentario;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public Integer getIdentificadorProp() {
        return identificadorProp;
    }

    public void setIdentificadorProp(Integer identificadorProp) {
        this.identificadorProp = identificadorProp;
    }
}
