package com.thermvaccine.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Vacina {

    private String id;
    private String nome;
    private float tempe_min;
    private float tempe_max;



    public Vacina(String nome, float tempe_min, float tempe_max){
        //id = autofill do SQL?
        this.nome = nome;
        this.tempe_min = tempe_min;
        this.tempe_max = tempe_max;
    }


    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public float getTempe_max() {
        return tempe_max;
    }

    public float getTempe_min() {
        return tempe_min;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTempe_max(float tempe_max) {
        this.tempe_max = tempe_max;
    }

    public void setTempe_min(float tempe_min) {
        this.tempe_min = tempe_min;
    }
}
