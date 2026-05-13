package com.thermvaccine;

import java.util.List;

import com.thermvaccine.controller.CaixaController;
import com.thermvaccine.model.RegistroDatalogger;
import com.thermvaccine.service.CalculoVidaUtilService;
import com.thermvaccine.service.DataLoggerService;

public class Main {

    public static void main(String[] args) {

        CaixaController caixaController = new CaixaController();

        caixaController.escolhaCaixa();
       
        // DataLoggerService dataLoggerService = new DataLoggerService();

        // dataLoggerService.limparBanco();
        
        // List<RegistroDatalogger> registros = dataLoggerService.leituraArquivo();

        // System.out.println("---------------------");

        // dataLoggerService.salvarRegistro(registros);

        // System.out.println("ESTA COM THREAD");

        // CalculoVidaUtilService.calcular(registros);
        
    }
}


    
