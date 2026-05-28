package com.thermvaccine.controller;

import java.util.List;
import java.util.Scanner;

import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;
import com.thermvaccine.model.DataLogger;
import com.thermvaccine.service.CaixaService;

public class CaixaController {

    private final CaixaService caixaService;

    public CaixaController(){
        this.caixaService = new CaixaService();
    }

    public List<Caixa> acharCaixas(List<Comanda> comandas, int qtdMax){

        List<Caixa> caixas = caixaService.acharCaixas(comandas);


        if(caixas.isEmpty()){
            System.out.println("Não há nenhuma caixa disponível");
            return null;
        }

        return caixas;

    }

    public void vincularDatalogger(List<Caixa> caixas){
        
            caixaService.vincularDatalogger(caixas);
    
        

    }
 

    public void inserirComandas(Caixa caixa, List<Comanda> comandas){

        caixaService.inserirComandas(caixa, comandas);
    }


    

}
