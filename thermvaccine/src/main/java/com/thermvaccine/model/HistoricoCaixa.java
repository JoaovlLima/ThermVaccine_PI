package com.thermvaccine.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class HistoricoCaixa {
    private String id;
    private LocalDateTime dataHora;
    private DataLogger dataLogger;
    private Caixa caixa;
    private String wifi;


    public HistoricoCaixa(DataLogger dataLogger, Caixa caixa, String wifi){
        this.id = UUID.randomUUID().toString();
        this.dataHora = LocalDateTime.now();
        this.dataLogger = dataLogger;
        this.caixa = caixa;
        this.wifi = wifi;

    }

}
