package com.thermvaccine.model;

public class Caixa {

    private long id;
    private int qtd_max_vac;

    public Caixa(long id, int qtd_max_vac){
        this.id = id;
        this.qtd_max_vac = qtd_max_vac;
    }
  

    public long getId() {
        return id;
    }

    public int getQtd_max_vac() {
        return qtd_max_vac;
    }


    public void setQtd_max_vac(int qtd_max_vac) {
        this.qtd_max_vac = qtd_max_vac;
    }

}
