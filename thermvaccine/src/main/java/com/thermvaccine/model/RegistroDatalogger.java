package com.thermvaccine.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistroDatalogger {

    private String id;
    
    private float temperatura;
    private Boolean rede;
    private float energia;
    private Boolean compressor;
    private Boolean alarme;
    private LocalDateTime data_hora;


         public RegistroDatalogger(float temperatura, Boolean rede, float energia,
         Boolean compressor, Boolean alarme){
            this.id = UUID.randomUUID().toString();;
            this.temperatura = temperatura;
            this.rede = rede;
            this.energia = energia;
            this.compressor = compressor;
            this.alarme = alarme;
         }


}
