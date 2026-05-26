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

    public Caixa vincularCaixa(List<Comanda> comandas, int qtdMax){

        List<Caixa> caixas = caixaService.acharCaixas(comandas);

        
        if(caixas.isEmpty()){
            System.out.println("Não há nenhuma caixa disponível");
            return null;
        }

        Scanner sc = new Scanner(System.in);

        return null;

        
    }

    public void vincularDatalogger(Caixa caixa, DataLogger dataLogger){

        caixaService.vincularDatalogger(dataLogger, caixa);

    }
 

    public void inserirComandas(Caixa caixa, List<Comanda> comandas){

        caixaService.inserirComandas(caixa, comandas);
    }


    

}
