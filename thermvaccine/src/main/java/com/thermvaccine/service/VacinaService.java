package com.thermvaccine.service;
import com.thermvaccine.model.Vacina;

public class VacinaService {

    void exibirDados(Vacina vacina){
        //System.out.printf("ID: %d\n", this.vacina.getId());
        System.out.printf("Nome: %s\n", vacina.getNome());
        System.out.printf("Temperatura mínima suportada: %f\n", vacina.getTempe_min());
        System.out.printf("Temperatura máxima suportada: %f\n", vacina.getTempe_max());

    }
}
