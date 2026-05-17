package com.thermvaccine.service;
import java.time.Duration;
import java.util.List;

import com.thermvaccine.repository.RegistroRepository;
import com.thermvaccine.repository.DataLoggerRepository;
import com.thermvaccine.model.DataLogger;
import com.thermvaccine.model.RegistroDatalogger;
import com.thermvaccine.model.Vacina;
import com.thermvaccine.repository.DataLoggerRepository;


public class CalculoVidaUtilService {

    public static final double R = 8.3144;
    public static final double MRNA_INICIAL = 100.0;

    private static final DataLoggerRepository repository = new DataLoggerRepository();

    public static void iniciar(String idDataLogger, Vacina vacina){
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
                double percentual = calcular(registros.subList(0, registros.size() - 1), ea, a, threshold);
                System.out.printf("mRNA intacto: %.6f%%%n", percentual);

            }

            try {
                Thread.sleep(5000);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        } 
    }


    public static double calcular(List<RegistroDatalogger> registros, double ea, double a, double threshold){

        double MRNA_Atual = MRNA_INICIAL;

        for(int i = 0; i < registros.size() - 1; i++){

            RegistroDatalogger atual = registros.get(i);
            RegistroDatalogger proximo = registros.get(i + 1);
            
            double deltaTSegundos = Duration.between(
                atual.getData_hora(),
                proximo.getData_hora()

            ).toSeconds();

            if (deltaTSegundos <= 0) continue;

            double tempKelvin = atual.getTemperatura() + 273.15;
            double k = a * Math.exp(-ea / (R * tempKelvin));
            MRNA_Atual = MRNA_Atual * Math.exp(-k * deltaTSegundos);

            double percentualIntacto = (MRNA_Atual / MRNA_INICIAL) * 100.0;

            if(percentualIntacto < threshold){
                break;
            }
        }

        return (MRNA_Atual / MRNA_INICIAL) * 100.0;

    }
}