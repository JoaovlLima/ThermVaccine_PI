package com.thermvaccine.service;

import com.thermvaccine.model.Transporte;
import com.thermvaccine.model.Caixa;

public class TransporteService {

    void exibirDados(Transporte transp){
        System.out.printf("Placa: %s\n", transp.getPlaca());
        System.out.printf("Capacidade: %d\n", transp.getCapacidade());
        System.out.printf("Data Saida: %s\n", transp.getData_saida());
        System.out.printf("Data Chegada: %s\n", transp.getData_chegada());
    }

    void exibirCaixas(Caixa caixa){
        System.out.printf("ID: %s\n", caixa.getId());
        System.out.printf("Quantidade maxima de Vacina: %s\n", caixa.getQtd_max_vac());
        
    }

}
