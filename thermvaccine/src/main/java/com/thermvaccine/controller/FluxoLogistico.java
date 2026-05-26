package com.thermvaccine.controller;

import java.util.Scanner;

import com.thermvaccine.model.UserLogistica;
import com.thermvaccine.utils.ConsoleUtils;
import com.thermvaccine.controller.CaixaController;
import com.thermvaccine.controller.TransporteController;

public class FluxoLogistico {

    Scanner sc = new Scanner(System.in);
    
    public void start(UserLogistica user){
        
        int op = 0;

        CaixaController caixaController = new CaixaController();
        TransporteController traspController = new TransporteController();

        System.out.println("Olá, "+user.getNome());
        System.out.println("");

        while(op != 5){


        System.out.println("Digite o número correspondente a o que você quer fazer: ");
        System.out.println("1-) Cadastrar Lote");
        System.out.println("2-) Criar Comanda");
        System.out.println("3-) Iniciar Transporte");
        System.out.println("4-) Sair");

        op = sc.nextInt();

        switch (op) {

            case 1: // Cadastro Lote
                

                continue;

            case 2: // Criar Comanda
                

                continue;

            // case 3: // Cadastrar Caixa

                // continue;


            case 3: // Escolher Caixa

                ConsoleUtils.clearScreen();
                // caixaController.escolhaCaixa();

                continue;

            case 4: // Cadastrar Transporte

                ConsoleUtils.clearScreen();
                traspController.criarTransporte();

                continue;
            
            case 5: // Escolher Transporte

                ConsoleUtils.clearScreen();
                traspController.escolhaTransporte();

                continue;

            case 6: // Sair
            
                break;

            default:

                System.out.println("Número Inválido. Favor digitar um valor de 1 a 5");
                System.out.println();

                continue;
                
            }

            

            
        }

        sc.close();
    }


    
}
