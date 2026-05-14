package com.thermvaccine.repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thermvaccine.model.HistoricoCaixa;

public class HistoricoCaixaRepository {
    private final ObjectMapper mapper = new ObjectMapper();

    private final File arquivo = new File("thermvaccine\\data\\HistoricoCaixa.json");


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
    public List<HistoricoCaixa> listar() {
        try {
            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                    arquivo,
                    new TypeReference<List<HistoricoCaixa>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // SAVE FILE
    public void salvar(List<HistoricoCaixa> historicoCaixa) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(arquivo, historicoCaixa);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
