package com.thermvaccine.model;

import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataLogger {
    private String id; 
    private String modelo; 
    private boolean disponivel;
    private List<RegistroDatalogger> registroDatalogger;

    

    public DataLogger(String modelo, List<RegistroDatalogger> registroDataloggers){
        this.id = UUID.randomUUID().toString();
        this.modelo = modelo;
        this.registroDatalogger = registroDataloggers;
        this.disponivel = true;
    }

}
