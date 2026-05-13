package com.thermvaccine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Caixa {

    private String id;
    private int qtd_max_vac;
    private List<Comanda> comandas = new ArrayList<>();
    private boolean disponivel;

    public Caixa(int qtd_max_vac){
        this.id = UUID.randomUUID().toString();
        this.qtd_max_vac = qtd_max_vac;
        this.disponivel = true;
    }

    public Caixa(int qtd_max_vac, List<Comanda> comandas){
        this.id = UUID.randomUUID().toString();
        this.qtd_max_vac = qtd_max_vac;
        this.comandas = comandas;
    }
  
     public List<Comanda> getComandas() {
        return comandas;
    }

    public void setId(String id) {
        this.id = id;
    }

     public void setDisponivel(boolean disponivel) {
         this.disponivel = disponivel;
     }

    public void setComandas(List<Comanda> comandas) {
        this.comandas = comandas;
    }

    public void inserirComandaUnidade(Comanda comanda){
        this.comandas.add(comanda);
    }

    public void inserirComandas(List<Comanda> comandas){
        this.comandas = comandas;
    }


    public boolean getDisponivel(){
        return disponivel;
    }

    public void setDiponivel(boolean disponivel){
        this.disponivel = disponivel;
    }

    public String getId() {
        return id;
    }

    public int getQtd_max_vac() {
        return qtd_max_vac;
    }

    public List<Comanda> getComandas() {
        return comandas;
    }

    public void setComandas(List<Comanda> comandas) {
        this.comandas = comandas;
    }


    public void setQtd_max_vac(int qtd_max_vac) {
        this.qtd_max_vac = qtd_max_vac;
    }

}
