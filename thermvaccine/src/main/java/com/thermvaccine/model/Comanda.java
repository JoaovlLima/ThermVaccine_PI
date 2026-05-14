package com.thermvaccine.model;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Comanda {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public enum StatusComanda{
        EM_TRANSITO,
        ENTREGUE,
        CANCELADO
    }


    private long id;
    private LocalDateTime data_emissao;
    private String cep;
    private int numEndereco;
    private StatusComanda status;
    private List<Lote_coman> lote_coman = new ArrayList<>();


  
    public Comanda(long id, String data_emissao, String cep, int numEndereco, StatusComanda status, List<Lote_coman> lote_coman){
        this.id = id;
        this.data_emissao = LocalDateTime.parse(data_emissao, FORMATTER);
        this.cep = cep;
        this.numEndereco = numEndereco;
        this.status = status;
        this.lote_coman = lote_coman;
    }

    public long getId() {
        return id;
    }

    public List<Lote_coman> getLote_coman() {
        return lote_coman;
    }

    public void setLote_coman(List<Lote_coman> lote_coman) {
        this.lote_coman = lote_coman;
    }

    public LocalDateTime getData_emissao() {
        return data_emissao;
    }

    public String getCep() {
        return cep;
    }

    public int getNumEndereco() {
        return numEndereco;
    }

    public StatusComanda getStatus() {
        return status;
    }


    public void setData_emissao(LocalDateTime data_emissao) {
        this.data_emissao = data_emissao;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setNumEndereco(int numEndereco) {
        this.numEndereco = numEndereco;
    }

    public void setStatus(StatusComanda status) {
        this.status = status;
    }




}

