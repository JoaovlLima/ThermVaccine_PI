package com.thermvaccine.repository;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thermvaccine.model.Vacina;

public class VacinaRepository {

    private final File arquivo =
            new File("thermvaccine\\data\\vacina.json");
            // /home/taviz/VsCode/PI - ThermVaccine/ThermVaccine_PI/thermvaccine/data/ARQUIVO.json

    private final ObjectMapper mapper =
            new ObjectMapper();

    // READ FILE
    public List<Vacina> listar() {

        try {

            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<Vacina>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // SAVE FILE
    public void salvar(List<Vacina> vacinas) {

        try {

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, vacinas);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}