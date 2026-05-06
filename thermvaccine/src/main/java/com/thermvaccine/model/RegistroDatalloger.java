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
    private Boolean energia;
    private Boolean compressor;
    private Boolean alarme;
    private LocalDateTime data_hora;

    

    public RegistroDatalloger(Long id, float temperatura, Boolean rede, Boolean energia,
         Boolean compressor, Boolean alarme, LocalDateTime data_hora){
            this.id = id;
            this.temperatura = temperatura;
            this.rede = rede;
            this.energia = energia;
            this.compressor = compressor;
            this.alarme = alarme;
            this.data_hora = data_hora;
         }

}
