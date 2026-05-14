package com.thermvaccine.service;
import java.time.Duration;
import java.util.List;

import com.thermvaccine.repository.RegistroRepository;
import com.thermvaccine.model.RegistroDatalogger;


public class CalculoVidaUtilService {

    public static final double R = 8.3144;
    public static final double EA = 100000.0;
    public static final double A = 6.04e12;
    public static final double MRNA_INICIAL = 100.0;
    public static final double THRESHOLD_PERCENT = 95.0;

    private static final RegistroRepository repository = new RegistroRepository();

    public static void iniciar(){
        int tamanhoAnterior = 0;

        while (true){
            List<RegistroDatalogger> registros = repository.listar();

            if(registros.size() > tamanhoAnterior) {
                tamanhoAnterior = registros.size();
                double percentual = calcular(registros.subList(0, registros.size() - 1));
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


    public static double calcular(List<RegistroDatalogger> registros){

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
            double k = A * Math.exp(-EA / (R * tempKelvin));
            MRNA_Atual = MRNA_Atual * Math.exp(-k * deltaTSegundos);

            double percentualIntacto = (MRNA_Atual / MRNA_INICIAL) * 100.0;

            if(percentualIntacto < THRESHOLD_PERCENT){
                break;
            }
        }

        return (MRNA_Atual / MRNA_INICIAL) * 100.0;

    }
}