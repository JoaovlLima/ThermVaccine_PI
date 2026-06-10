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
import com.thermvaccine.model.Transporte;

public class TransporteRepository implements IRepository<Transporte> {
    
    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final File arquivo = new File("thermvaccine\\data\\transporte.json");


    // private final File arquivo =
    //     new File("/home/taviz/VsCode/PI - ThermVaccine/ThermVaccine_PI/thermvaccine/data/transporte.json");

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
    public List<Transporte> listar() {
        try {
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<Transporte>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // SAVE FILE
    public void salvar(List<Transporte> transporte) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, transporte);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EDIT FILE
    public void editar(Transporte transporteAtualizada){
        try {
            List<Transporte> transportes = listar();
            

            for (int i = 0; i < transportes.size(); i++) {
                if(transportes.get(i).getPlaca().equals(transporteAtualizada.getPlaca())){
                    transportes.set(i, transporteAtualizada);
                    break;
                }
            }


            salvar(transportes);
         } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FIND ID (PLACA)
    public Transporte findById(String placa){

        List<Transporte> TransporteDb = listar();

        for (int i = 0; i < TransporteDb.size(); i++) {
            if(TransporteDb.get(i).getPlaca().equals(placa)){
                return TransporteDb.get(i);
            }
        }

        return null;
    }

    

}
