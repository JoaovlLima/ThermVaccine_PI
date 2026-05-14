package com.thermvaccine.model;

public class Empresa {

    private String cnpj;
    private String nome;
    private String sede;

    public Empresa(String cnpj, String nome, String sede){
        this.cnpj = cnpj;
        this.nome = nome;
        this.sede = sede;
    }
 

    public String getCnpj() {
        return cnpj;
    }

    public String getNome() {
        return nome;
    }

    public String getSede() {
        return sede;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

}
