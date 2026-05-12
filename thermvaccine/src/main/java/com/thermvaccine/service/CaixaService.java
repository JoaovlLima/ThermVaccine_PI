package com.thermvaccine.service;

import java.util.List;
import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;

public class CaixaService {

    private ComandaService cs = new ComandaService();

    public void exibirComanda(Caixa caixa, List<Comanda> comandas){

        for(Comanda comanda : comandas){
            if(comanda.getCaixa().getId() == caixa.getId()){
                cs.exibirDados(comanda);                
            }
        }
  
    }

}
