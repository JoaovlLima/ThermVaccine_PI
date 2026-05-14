package com.thermvaccine.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Lote {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private long id;
    private int quantidade;
    private String fabricante;
    private LocalDateTime validade;
    private Usuario usuario;
    private Vacina vacina;

    public Lote(int quantidade, String fabricante, String validade, Usuario usuario, Vacina vacina){
        this.quantidade = quantidade;
        this.fabricante = fabricante;
        this.validade = LocalDateTime.parse(validade, FORMATTER);
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

    public String getValidade(){
        return validade.format(FORMATTER);
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

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }


}
