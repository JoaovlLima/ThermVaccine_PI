package com.thermvaccine.model;

import java.time.format.DateTimeFormatter;
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
    private Caixa caixa;
    private Lote lote;


    public Comanda(long id, String data_emissao, String cep, int numEndereco, StatusComanda status, Lote lote, Caixa caixa){
        this.id = id;
        this.data_emissao = LocalDateTime.parse(data_emissao, FORMATTER);
        this.cep = cep;
        this.numEndereco = numEndereco;
        this.status = status;
        this.lote = lote;
        this.caixa = caixa;
    }

    public long getId() {
        return id;
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

    public Lote getLote() {
        return lote;
    }

    public Caixa getCaixa() {
        return caixa;
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

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }




}

