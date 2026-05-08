package com.thermvaccine.service;
import com.thermvaccine.model.Lote;

public class LoteService {

    void exibirDados(Lote lote){
        //System.out.printf("ID: %d\n", this.lote.getId());
        System.out.printf("Fabricante: %s\n", lote.getFabricante());
        System.out.printf("Validade: %s\n", lote.getValidade());
        System.out.printf("Usuario: %s\n", lote.getUsuario());
        System.out.printf("Vacina: %f\n", lote.getVacina());
        //talvez pegar apenas o nome do usuario e vacina, em vez de printar eles inteiramente?

    }
}
  