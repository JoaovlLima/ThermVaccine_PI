package com.thermvaccine.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Transporte {
 
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String placa;
    private int capacidade;
    private LocalDateTime data_saida;
    private LocalDateTime data_chegada;
    private List<Caixa> caixa = new ArrayList<>();


    public Transporte(String placa, int capacidade, String data_saida){
        this.placa = placa;
        this.capacidade = capacidade;
        this.data_saida = LocalDateTime.parse(data_saida, FORMATTER);
    }


    public String getPlaca() {
        return placa;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public String getData_saida() {
        return data_saida.format(FORMATTER);
    }

    public String getData_chegada() {
        return data_chegada.format(FORMATTER);
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

    public void addUmaCaixa(Caixa caixa){
        this.caixa.add(caixa);
    }

}
