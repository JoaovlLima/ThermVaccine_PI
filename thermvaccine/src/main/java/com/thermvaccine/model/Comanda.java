package com.thermvaccine.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Comanda {


    private String id;
    private LocalDateTime data_emissao;
    private LocalDateTime data_saida;
    private LocalDateTime data_Chegada;
    private String cep;
    private int numEndereco;
    private StatusComanda status;
    
    private String idCaixa;
    private List<Lote_coman> lote_coman = new ArrayList<>();


    public LocalDateTime getData_saida() {
        return data_saida;
    }

    public void setData_saida(LocalDateTime data_saida) {
        this.data_saida = data_saida;
    }

    public LocalDateTime getData_Chegada() {
        return data_Chegada;
    }

    public void setData_Chegada(LocalDateTime data_Chegada) {
        this.data_Chegada = data_Chegada;
    }

    public String getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(String idCaixa) {
        this.idCaixa = idCaixa;
    }

    public enum StatusComanda{
        EM_AGUARDO,
        EM_TRANSITO,
        ENTREGUE,
        CANCELADO
    }

  
    public Comanda(String cep, int numEndereco, List<Lote_coman> lote_coman){
        this.id = UUID.randomUUID().toString();
        this.data_emissao = LocalDateTime.now();
        this.cep = cep;
        this.numEndereco = numEndereco;
        this.status = StatusComanda.EM_AGUARDO;
        this.lote_coman = lote_coman;
    }

    public String getId() {
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

