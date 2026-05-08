package com.thermvaccine.service;
import java.time.Duration;
import java.util.List;

import com.thermvaccine.model.RegistroDatalloger;


public class CalculoVidaUtilService {

    public static final double R = 8.3144;
    public static final double EA = 100000.0;
    public static final double A = 6.04e12;
    public static final double MRNA_INICIAL = 100.0;
    public static final double THRESHOLD_PERCENT = 70.0;


    public static double calcular(List<RegistroDatalloger> registros){

        double MRNA_Atual = MRNA_INICIAL;
    

        for(int i = 0; i < registros.size() - 1; i++){

            RegistroDatalloger atual = registros.get(i);
            RegistroDatalloger proximo = registros.get(i + 1);
            
            double deltaTSegundos = Duration.between(
                atual.getData_hora(),
                proximo.getData_hora()

            ).toSeconds();

            double tempKelvin = atual.getTemperatura() + 273.15;

            double k = A * Math.exp(-EA / (R * tempKelvin));

            MRNA_Atual = MRNA_Atual * Math.exp(-k * deltaTSegundos);

            

        }
        
        double percentualIntacto = (MRNA_Atual / MRNA_INICIAL) * 100.0;
        System.out.printf("mRNA intacto: %.2f%%\n", percentualIntacto);

        return percentualIntacto;

    }



}
