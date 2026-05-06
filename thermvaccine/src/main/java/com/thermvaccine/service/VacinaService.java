package com.thermvaccine.service;
import com.thermvaccine.model.Vacina;

public class VacinaService {

    private Vacina vacina;

    public VacinaService(Vacina vacina){
        this.vacina = vacina;
    }

    void exibirDados(Vacina vacina){
        //System.out.printf("ID: %d\n", this.vacina.getId());
        System.out.printf("Nome: %s\n", this.vacina.getNome());
        System.out.printf("Temperatura mínima suportada: %f\n", this.vacina.getTempe_min());
        System.out.printf("Temperatura máxima suportada: %f\n", this.vacina.getTempe_max());

    }
}
