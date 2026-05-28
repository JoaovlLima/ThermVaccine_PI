package com.thermvaccine.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thermvaccine.model.Comanda;


public class ComandaRepository implements IRepository<Comanda> {
    
    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final File arquivo = new File("thermvaccine\\data\\comanda.json");


    // CLEANER
    public void limpar() {
    try {
        arquivo.getParentFile().mkdirs();
        mapper.writeValue(arquivo, new ArrayList<>());
        System.out.println("Arquivo limpo com sucesso!");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    // READ FILE
    public List<Comanda> listar() {
        try {
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<Comanda>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // SAVE FILE
    public void salvar(List<Comanda> Comanda) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, Comanda);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // EDIT FILE
    public void editar(Comanda comandaAtualizada){
        try {
            List<Comanda> comandas = listar();
            

            for (int i = 0; i < comandas.size(); i++) {
                if(comandas.get(i).getId().equals(comandaAtualizada.getId())){
                    comandas.set(i, comandaAtualizada);
                    break;
                }
            }


            salvar(comandas);
         } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FIND ID
    public Comanda findById(String idComanda){

        List<Comanda> comandaDb = listar();

        for (int i = 0; i < comandaDb.size(); i++) {
            if(comandaDb.get(i).getId().equals(idComanda)){
                return comandaDb.get(i);
            }
        }

        return null;
    }

  public List<Comanda> comandasPorIdCaixa(String idCaixa){

    List<Comanda> comandasBd = listar();

    List<Comanda> comandasPorCaixa = new ArrayList<>();;

    for (Comanda comanda : comandasBd) {
        
        if(comanda.getIdCaixa().equals(idCaixa)){
            comandasPorCaixa.add(comanda);
        }
    }

    return comandasPorCaixa;
    
  }
}
