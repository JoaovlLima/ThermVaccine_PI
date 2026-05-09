package com.thermvaccine;

import java.util.List;

import com.thermvaccine.model.RegistroDatalloger;
import com.thermvaccine.service.CalculoVidaUtilService;
import com.thermvaccine.service.DataLoggerService;

public class Main {

    public static void main(String[] args) throws InterruptedException {
       
        DataLoggerService dataLoggerService = new DataLoggerService();

        dataLoggerService.limparBanco();
        
        List<RegistroDatalloger> registros = dataLoggerService.leituraArquivo();

        System.out.println("---------------------");

        dataLoggerService.salvarRegistro(registros);

        System.out.println("ESTA COM THREAD");

        CalculoVidaUtilService.iniciar();
        
    }
}


    
