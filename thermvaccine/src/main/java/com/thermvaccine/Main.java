package com.thermvaccine;

import java.util.List;

import com.thermvaccine.model.RegistroDatalloger;
import com.thermvaccine.service.DataLoggerService;

public class Main {

    public static void main(String[] args) {
        DataLoggerService dataService = new DataLoggerService();
        List<RegistroDatalloger> registros = dataService.leituraArquivo();

        System.out.println("Quantidade : "+registros.size());

    }
}