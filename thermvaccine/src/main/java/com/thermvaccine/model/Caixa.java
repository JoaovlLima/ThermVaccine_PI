package com.thermvaccine.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Caixa {

    private String id;
    private int qtd_max_vac;


    public Caixa(String id, int qtd_max_vac){
        this.id = id;
        this.qtd_max_vac = qtd_max_vac;
    }
  

    public String getId() {
        return id;
    }

    public int getQtd_max_vac() {
        return qtd_max_vac;
    }


    public void setQtd_max_vac(int qtd_max_vac) {
        this.qtd_max_vac = qtd_max_vac;
    }

}
