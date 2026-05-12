package com.thermvaccine.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Lote_coman {
//excluir dps
    private String id;
    private Comanda comanda;
    private Lote lote;
    private int qtd_vacina;

    public Lote_coman(String id, Comanda comanda, Lote lote, int qtd_vacina){
        this.id = id;
        this.comanda = comanda;
        this.lote = lote;
        this.qtd_vacina = qtd_vacina;
    }


    public String getId() {
        return id;
    }

    public Comanda getComanda() {
        return comanda;
    }

    public Lote getLote() {
        return lote;
    }
    
    public int getQtd_vacina() {
        return qtd_vacina;
    }


    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public void setQtd_vacina(int qtd_vacina) {
        this.qtd_vacina = qtd_vacina;
    }


}


