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
    private Boolean disponivel;


    public Transporte(String placa, int capacidade){
        this.placa = placa;
        this.capacidade = capacidade;
        this.disponivel = true;
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

}
