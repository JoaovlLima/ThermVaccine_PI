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
import com.thermvaccine.model.DataLogger;

public class DataLoggerRepository {


    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private final File arquivo = new File("/home/taviz/VsCode/PI - ThermVaccine/ThermVaccine_PI/thermvaccine/data/dataLogger.json");


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
    public List<DataLogger> listar() {
        try {
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<DataLogger>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // SAVE FILE
    public void salvar(List<DataLogger> dataLogger) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, dataLogger);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // EDIT FILE
    public void editar(DataLogger dataLoggerAtualizada){
        try {
            List<DataLogger> dataLoggers = listar();
            

            for (int i = 0; i < dataLoggers.size(); i++) {
                if(dataLoggers.get(i).getId().equals(dataLoggerAtualizada.getId())){
                    dataLoggers.set(i, dataLoggerAtualizada);
                }
            }


            salvar(dataLoggers);
         } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // FIND ID
    public DataLogger findById(String idDataLogger){

        List<DataLogger> dataLoggersDb = listar();

        for (int i = 0; i < dataLoggersDb.size(); i++) {
            if(dataLoggersDb.get(i).getId().equals(idDataLogger)){
                return dataLoggersDb.get(i);
            }
        }

        return null;
    }
}
