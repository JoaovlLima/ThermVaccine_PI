package com.thermvaccine;

import java.util.List;

import com.thermvaccine.model.RegistroDatalloger;
import com.thermvaccine.service.CalculoVidaUtilService;
import com.thermvaccine.service.DataLoggerService;

public class Main {

    public static void main(String[] args) {
       
        DataLoggerService dataLoggerService = new DataLoggerService();

        List<RegistroDatalloger> registros = dataLoggerService.leituraArquivo();

        System.out.println("---------------------");

        CalculoVidaUtilService.calcular(registros);
        
    }
}