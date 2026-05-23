package com.thermvaccine.model;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Transporte {
 
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String placa;
    private int capacidade;
    private LocalDateTime data_saida;
    private LocalDateTime data_chegada;
    private Boolean disponivel;


    public Transporte(String placa, int capacidade){
        this.placa = placa;
        this.capacidade = capacidade;
        this.disponivel = true;
        this.data_saida = null;
        this.data_chegada = null;
    }


    public Boolean getDisponivel() {
        return disponivel;
    }


    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }


    public String getPlaca() {
        return placa;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public String getData_saida() {
        if(this.data_saida == null){
            return null;
        }
        return data_saida.format(FORMATTER);
    }

    public String getData_chegada() {
        if(this.data_chegada == null){
            return null;
        }
        return data_chegada.format(FORMATTER);
    }

    public void setData_chegada(LocalDateTime data_chegada) {
        this.data_chegada = data_chegada;
    }

}
