package com.thermvaccine.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Lote_coman {
    private String id;
    private Lote lote;
    private int qtd;
    private double MRNA_Disponivel;

}
