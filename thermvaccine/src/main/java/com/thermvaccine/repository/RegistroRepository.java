package com.thermvaccine.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thermvaccine.model.RegistroDatalloger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistroRepository {

    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final File arquivo = new File("/home/taviz/VsCode/PI - ThermVaccine/ThermVaccine_PI/thermvaccine/data/registro.json");


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
    public List<RegistroDatalloger> listar() {
        try {
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<RegistroDatalloger>>() {}
            );


        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // SAVE FILE
    public void salvar(List<RegistroDatalloger> registro) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, registro);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}