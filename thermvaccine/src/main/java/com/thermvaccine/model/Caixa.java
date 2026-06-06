package com.thermvaccine.model;

import java.util.UUID;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Caixa {

    private String id;
    private int qtd_max_vac;
    private Boolean disponivel;
    private String idTransporte;

    public Caixa(int qtd_max_vac){
        this.id = UUID.randomUUID().toString();
        this.qtd_max_vac = qtd_max_vac;
        this.disponivel = true;
    }


    public String getIdTransporte() {
        return idTransporte;
    }


    public void setIdTransporte(String idTransporte) {
        if(idTransporte != null){
            this.disponivel = false;
        }
        this.idTransporte = idTransporte;
    }


    public void setId(String id) {
        this.id = id;
    }

     public void setDisponivel(Boolean disponivel) {
        if(disponivel == true){
            this.idTransporte = null;
        }
         this.disponivel = disponivel;
     }


    public Boolean getDisponivel(){
        return disponivel;
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
