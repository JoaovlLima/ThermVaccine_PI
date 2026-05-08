package com.thermvaccine.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistroDatalloger {

    private Long id;
    
    private float temperatura;
    private Boolean rede;
    private float energia;
    private Boolean compressor;
    private Boolean alarme;
    private LocalDateTime data_hora;

    

    public RegistroDatalloger(Long id, float temperatura, Boolean rede, float energia,
         Boolean compressor, Boolean alarme){
            this.id = id;
            this.temperatura = temperatura;
            this.rede = rede;
            this.energia = energia;
            this.compressor = compressor;
            this.alarme = alarme;
         }

}
