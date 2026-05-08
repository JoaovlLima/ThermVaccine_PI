package com.thermvaccine.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thermvaccine.model.RegistroDatalloger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistroRepository {

    private final ObjectMapper mapper = new ObjectMapper();
    private final File arquivo = new File("data/registro.json");

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