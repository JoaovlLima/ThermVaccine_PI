package com.thermvaccine.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Usuario {
    //extend pessoa?
    private String id;
    private String re;
    private String nome;
    private String senha;
    private String cargo;
    private Empresa empresa;
    private Tier tier;


    public enum Tier{
        LOG,QUA,ADM
    }

    public Usuario(String re, String nome, String senha, String cargo, Tier tier){
        this.re = re;
        this.nome = nome;
        this.senha = senha;
        this.cargo = cargo;
        this.tier = tier;

    }


    public String getId() {
        return id;
    }

    public Tier getTier() {
        return tier;
    }


    public void setTier(Tier tier) {
        this.tier = tier;
    }


    public String getRe() {
        return re;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public String getCargo() {
        return cargo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }



    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }



    
}
