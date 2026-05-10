package com.thermvaccine.service;

import java.util.List;
import java.time.format.DateTimeFormatter;
import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;

public class CaixaService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void exibirComanda(Caixa caixa, List<Comanda> comandas){

        for(Comanda comanda : comandas){
            if(comanda.getCaixa().getId() == caixa.getId()){

                System.out.printf("ID: %d\n", comanda.getId());
                System.out.printf("Data de emissao: %s\n", comanda.getData_emissao().format(FORMATTER));
                System.out.printf("Status: %s\n", comanda.getStatus());
                System.out.printf("Local de entrega: CEP - %s | Numero da residencia - %d\n", comanda.getCep(), comanda.getNumEndereco());
                System.out.printf("Referente ao lote: %d", comanda.getLote().getId());
                
            }
        }
  
    }

}
