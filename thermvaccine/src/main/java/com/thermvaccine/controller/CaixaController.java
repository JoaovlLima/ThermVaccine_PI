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

    public Caixa escolhaCaixa(){



        List<Caixa> caixas = caixaService.listarCaixasDisponiveis();

        if(caixas.isEmpty()){
            return null;
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("\nOpções disponiveis: ");
        for (int i = 0; i < caixas.size(); i++) {

            System.out.println("\n"+(i+1)+"-Caixa "+(i+1)+" | Quantidade de espaço: "+caixas.get(i).getQtd_max_vac());
        }
        
        System.out.println("\nEscolha uma opção: ");
        int escolhaCaixa = sc.nextInt();
        sc.close();

        if(escolhaCaixa >= 1 && escolhaCaixa <= caixas.size()){
            System.out.println("Você escolheu : "+caixas.get(escolhaCaixa-1).getId());
            return caixas.get(escolhaCaixa-1); 
        }
        
        System.out.println("Opção invalida");
        System.out.println("SIZE :"+caixas.size());
        return null;

        
    }

    public void vincularDatalogger(Caixa caixa, DataLogger dataLogger){

        caixaService.vincularDatalogger(dataLogger, caixa);

    }
 

    public void inserirComandas(Caixa caixa, List<Comanda> comandas){

        caixaService.inserirComandas(caixa, comandas);;
    }


    

}
