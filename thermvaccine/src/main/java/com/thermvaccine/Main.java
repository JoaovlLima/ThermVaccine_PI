package com.thermvaccine;

import com.thermvaccine.model.Vacina;
import com.thermvaccine.service.VacinaService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        VacinaService service =
                new VacinaService();

        service.criar(
                new Vacina("Pfizer",2.0f, 8.0f)
        );

        System.out.println("\nLISTA:");
        service.listar();

        service.atualizar(1,
                "Pfizer Bivalente");

        System.out.println("\nATUALIZADO:");
        service.listar();

        service.deletar(2);

        System.out.println("\nDEPOIS DELETE:");
        service.listar();
    }
}


    
