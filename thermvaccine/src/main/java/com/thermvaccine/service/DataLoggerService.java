package com.thermvaccine.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.thermvaccine.model.RegistroDatalloger;

public class DataLoggerService {

    public List<RegistroDatalloger> leituraArquivo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<RegistroDatalloger> registros = new ArrayList<>();

        InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("planilha_datalogger.csv");

        if (input == null) {
            throw new RuntimeException("Arquivo CSV não encontrado");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String linha;
            br.readLine();

            while ((linha = br.readLine()) != null) {

                String[] valores = linha.split(";");

                Long id = Long.parseLong(valores[0]);
                LocalDateTime data_hora = LocalDateTime.parse(valores[1], formatter);
                float temperatura = Float.parseFloat(valores[2]);
                boolean energia = Boolean.parseBoolean(valores[4]);
                boolean rede = Boolean.parseBoolean(valores[5]);
                boolean alarme = Boolean.parseBoolean(valores[7]);
                boolean compressor = Boolean.parseBoolean(valores[8]);

                RegistroDatalloger registro = new RegistroDatalloger(id, temperatura, rede, energia, compressor,
                        alarme, data_hora);

                registros.add(registro);
                System.out.println(id + " - " + temperatura);
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

        return registros;
    }

}

// Indice;Data_Hora;T1_C;T2_C;Bateria_V;Rede;Porta;Alarme;Compressor;Status
// ->csv
// id,temperatura,rede,energia,compressor,alarme,data_hora -> entidade