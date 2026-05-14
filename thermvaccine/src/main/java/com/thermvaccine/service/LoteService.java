package com.thermvaccine.service;
import com.thermvaccine.model.Lote;

public class LoteService {

    public void exibirDados(Lote lote){
        //System.out.printf("ID: %d\n", this.lote.getId());
        System.out.printf("Fabricante: %s\n", lote.getFabricante());
        System.out.printf("Validade: %s\n", lote.getValidade());
        System.out.printf("Usuario: %s\n", lote.getUsuario().getNome());
        System.out.printf("Vacina: %s\n", lote.getVacina().getNome());
        //talvez pegar apenas o nome do usuario e vacina, em vez de printar eles inteiramente?

    }
}
  