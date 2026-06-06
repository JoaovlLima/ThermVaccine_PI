package com.thermvaccine.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thermvaccine.model.DataLogger;
import com.thermvaccine.model.Lote;

public class LoteRepository {
    
    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final File arquivo = new File("thermvaccine\\data\\lote.json");


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
    public List<Lote> listar() {
        try {
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<Lote>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // SAVE FILE
    public void salvar(List<Lote> lotes) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, lotes);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EDIT FILE
    public void editar(Lote loteAtualizada){
        try {
            List<Lote> lotes = listar();
            

            for (int i = 0; i < lotes.size(); i++) {
                if(lotes.get(i).getId().equals(loteAtualizada.getId())){
                    lotes.set(i, loteAtualizada);
                    break;
                }
            }


            salvar(lotes);
         } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FIND ID
    public Lote findById(String idLote){

        List<Lote> lotesDb = listar();

        for (int i = 0; i < lotesDb.size(); i++) {
            if(lotesDb.get(i).getId().equals(idLote)){
                return lotesDb.get(i);
            }
        }

        return null;
    }

   
}
