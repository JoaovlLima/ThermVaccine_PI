package com.thermvaccine.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Vacina {

    private String id;
    private String nome;
    private float tempe_min;
    private float tempe_max;

    private double ea;        // Energia de ativação (J/mol) 
    private double a;         // Fator pré-exponencial (s⁻¹) - em segundos
    private double threshold; // % minímo de mRNA intacto



    public Vacina(String nome, float tempe_min, float tempe_max, double ea, double a, double threshold){
        //id = autofill do SQL?
        this.nome = nome;
        this.tempe_min = tempe_min;
        this.tempe_max = tempe_max;
        this.ea = ea;
        this.a = a;
        this.threshold = threshold;
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

    public double getEa() {
        return ea;
    }

    public void setEa(double ea) {
        this.ea = ea;
    }

    public double getA() {
        return a;
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

        public void setA(double a) {
        this.a = a;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

}
