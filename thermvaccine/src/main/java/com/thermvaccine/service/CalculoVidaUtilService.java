package com.thermvaccine.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.thermvaccine.repository.RegistroRepository;
import com.thermvaccine.repository.DataLoggerRepository;
import com.thermvaccine.model.DataLogger;
import com.thermvaccine.model.RegistroDatalogger;
import com.thermvaccine.model.Vacina;
import com.thermvaccine.repository.DataLoggerRepository;


public class CalculoVidaUtilService implements ICalculo{

    public static final double R = 8.3144;
    public static final double MRNA_INICIAL = 100.0;

    private static final DataLoggerRepository repository = new DataLoggerRepository();

    public void iniciar(String idDataLogger, Vacina vacina, double mrnaInicial){
        int tamanhoAnterior = 0;

        double ea = vacina.getEa();
        double a = vacina.getA();
        double threshold = vacina.getThreshold();


        while (true){
            DataLogger dataLogger = repository.findById(idDataLogger);

            if (dataLogger == null) {
                System.out.println("DataLogger não encontrado: " + idDataLogger);
                break;
            }

            List<RegistroDatalogger> registros = dataLogger.getRegistroDatalogger();

            if(registros.size() > tamanhoAnterior) {
                tamanhoAnterior = registros.size();
                double percentual = calcularRegistros(registros.subList(0, registros.size() - 1), ea, a, threshold, mrnaInicial);
                System.out.printf("mRNA intacto: %.6f%%%n", percentual);

                if(percentual < threshold){
                    System.out.println("VACINA VENCIDA.");
                    break;
                }
            }

            try {
                Thread.sleep(5000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        } 
    }


    public double calcularRegistros(List<RegistroDatalogger> registros, double ea, double a, double threshold, double mrnaInicial){

        double MRNA_Atual = (mrnaInicial / 100) * MRNA_INICIAL;

        for(int i = 0; i < registros.size() - 1; i++){ // Trocar para ser a partir do último registro gerado - não vão ser apagados

            RegistroDatalogger atual = registros.get(i);
            RegistroDatalogger proximo = registros.get(i + 1);
            
            double deltaTSegundos = Duration.between(
                atual.getData_hora(),
                proximo.getData_hora()

            ).toSeconds();


            if (deltaTSegundos <= 0) continue;

            // Corto a função daqui pra baixo, essa função atual fica pra listar os registros e aplicar os segundos, a que vou chamar, calcular, fica a parte para melhor utilização (modular)

        
            MRNA_Atual = calcular(deltaTSegundos, ea, a, threshold, MRNA_Atual, registros.get(i).getTemperatura());
            
        }

        return (MRNA_Atual / MRNA_INICIAL) * 100.0;
    }

    public Double calcularMRNADisponivel(Vacina vacina,  LocalDateTime data_descon){

        
        double deltaTSegundos = Duration.between(
            data_descon,
            LocalDateTime.now()

        ).toSeconds();

        double temp_med = (vacina.getTempe_max() + vacina.getTempe_min()) / 2;

        return calcular(deltaTSegundos, vacina.getEa(), vacina.getA(), vacina.getThreshold(), MRNA_INICIAL, temp_med);

    }

    public Double calcular(double deltaTSegundos, double ea, double a, double threshold, double MRNA_Atual, double temp){
         
        double tempKelvin = temp + 273.15;
            double k = a * Math.exp(-ea / (R * tempKelvin));
            MRNA_Atual = MRNA_Atual * Math.exp(-k * deltaTSegundos);

            return MRNA_Atual;
    }
}