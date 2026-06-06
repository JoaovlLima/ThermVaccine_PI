package com.thermvaccine.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;
import com.thermvaccine.model.DataLogger;

public class CaixaRepository implements IRepository<Caixa> {

    private final ObjectMapper mapper = new ObjectMapper();

    private final File arquivo = new File("thermvaccine\\data\\caixa.json");


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
    public List<Caixa> listar() {
        try {
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<Caixa>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // SAVE FILE

    public void salvar(List<Caixa> caixa) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, caixa);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EDIT FILE
    public void editar(Caixa caixaAtualizada){
        try {
            List<Caixa> caixas = listar();

            for (int i = 0; i < caixas.size(); i++) {
                if(caixas.get(i).getId().equals(caixaAtualizada.getId())){
                    caixas.set(i, caixaAtualizada);
                }
            }

            salvar(caixas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // GET Caixa
    public Caixa pegarCaixa(Caixa caixa){
        try {
            
            List<Caixa> caixas = listar();

            for (Caixa c : caixas) {
                if(c.getId().equals(caixa.getId())){
                    return c;
                }
            }
            return null;

        } catch (Exception e) {
             throw new RuntimeException("Erro ao pegar Caixa");
        }
    }

     // FIND ID
    public Caixa findById(String idCaixa){

        List<Caixa> caixasDb = listar();

        for (int i = 0; i < caixasDb.size(); i++) {
            if(caixasDb.get(i).getId().equals(idCaixa)){
                return caixasDb.get(i);
            }
        }

        return null;
    }

    public List<Caixa> caixasPorPlacaTransporte(String placa){
    List<Caixa> caixasBd = listar();

    List<Caixa> caixasPorTransporte = new ArrayList<>();

    for (Caixa caixa : caixasBd) {
        
        if(caixa.getIdTransporte() != null && caixa.getIdTransporte().equals(placa)){
            caixasPorTransporte.add(caixa);
        }
    } 

    return caixasPorTransporte;
    
  }
}
