package com.thermvaccine.controller;

import java.util.List;
import java.util.Scanner;

import com.thermvaccine.model.Caixa;
import com.thermvaccine.service.CaixaService;

public class CaixaController {

    private final CaixaService caixaService;

    public CaixaController(){
        this.caixaService = new CaixaService();
    }

    public void criacaoCaixa(){


        Scanner sc = new Scanner(System.in);

        List<Caixa> caixas = caixaService.listarCaixasDisponiveis();

        if(caixas.isEmpty()){
            return;
        }

        System.out.println("\nOpções disponiveis: ");
        for (int i = 0; i < caixas.size(); i++) {

            System.out.println("\n"+(i+1)+"-Caixa "+(i+1)+" | Quantidade de espaço: "+caixas.get(i).getQtd_max_vac());
        }
        
        System.out.println("\nEscolha uma opção: ");
        int escolhaCaixa = sc.nextInt();

        if(escolhaCaixa >= 1 && escolhaCaixa <= caixas.size()){
            System.out.println("Você escolheu : "+caixas.get(escolhaCaixa-1).getId());
        }else{
            System.out.println("Opção invalida");
            System.out.println("SIZE :"+caixas.size());
            return;
        }

    }
}
