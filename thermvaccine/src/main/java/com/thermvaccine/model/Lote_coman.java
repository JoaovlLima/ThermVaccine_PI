package com.thermvaccine.model;

import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Lote_coman {
    private String id;
    private Lote lote;
    private int qtd;
    private double MRNA_Disponivel;



    public Lote_coman(Lote lote, int qtd){
        this.id = UUID.randomUUID().toString();
        this.lote = lote;
        this.qtd = qtd;
    }

}
