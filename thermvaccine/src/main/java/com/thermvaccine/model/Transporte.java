package com.thermvaccine.model;

import java.time.LocalDateTime;
import java.util.List;

public class Transporte {

    private String placa;
    private int capacidade;
    private LocalDateTime data_saida;
    private LocalDateTime data_chegada;
    private List<Caixa> caixa;


    public Transporte(String placa, int capacidade, LocalDateTime data_saida){
        this.placa = placa;
        this.capacidade = capacidade;
        this.data_saida = data_saida;
    }


    public String getPlaca() {
        return placa;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public LocalDateTime getData_saida() {
        return data_saida;
    }

    public LocalDateTime getData_chegada() {
        return data_chegada;
    }

    public List<Caixa> getCaixa() {
        return caixa;
    }



    public void setData_chegada(LocalDateTime data_chegada) {
        this.data_chegada = data_chegada;
    }

    public void setCaixa(List<Caixa> caixa) {
        this.caixa = caixa;
    }
}
