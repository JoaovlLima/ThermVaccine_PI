package com.thermvaccine.model;
//import java.time.format.DateTimeFormatter; <- é esse mesmo

public class Lote {

    private long id;
    private int quantidade;
    private String fabricante;
    //qual datetime?
    private Usuario usuario;
    private Vacina vacina;

    public Lote(int quantidade, String fabricante, /*validade*/ Usuario usuario, Vacina vacina){
        this.quantidade = quantidade;
        this.fabricante = fabricante;
        //validade 
        this.usuario = usuario;
        this.vacina = vacina;
    }


    public long getId() {
        return id;
    }

    public String getFabricante() {
        return fabricante;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Vacina getVacina() {
        return vacina;
    }



    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }


}
