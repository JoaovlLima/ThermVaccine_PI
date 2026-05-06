package com.thermvaccine.service;
import com.thermvaccine.model.Lote;

public class LoteService {

    private Lote lote;

    public LoteService(Lote lote){
        this.lote = lote;
    }

    void exibirDados(Lote lote){
        //System.out.printf("ID: %d\n", this.lote.getId());
        System.out.printf("Fabricante: %s\n", this.lote.getFabricante());
        System.out.printf("Validade: %s\n", this.lote.getValidade());
        System.out.printf("Usuario: %s\n", this.lote.getUsuario());
        System.out.printf("Vacina: %f\n", this.lote.getVacina());
        //talvez pegar apenas o nome do usuario e vacina, em vez de printar eles inteiramente?

    }
}
