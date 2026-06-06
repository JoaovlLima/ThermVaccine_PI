package com.thermvaccine.model;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Lote {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private String id;
    private int quantidade;
    private String fabricante;
    private LocalDateTime data_descongelamento;
    private LocalDateTime validade;

    private UserLogistica usuario;
    private Vacina vacina;


    public Lote(int quantidade, String fabricante, LocalDateTime validade, LocalDateTime data_descongelamento, UserLogistica usuario, Vacina vacina){
        this.id = UUID.randomUUID().toString();
        this.quantidade = quantidade;
        this.fabricante = fabricante;
        this.validade = validade;
        this.data_descongelamento = data_descongelamento;
        this.usuario = usuario;
        this.vacina = vacina;
    }


    public String getId() {
        return id;
    }

    public String getFabricante() {
        return fabricante;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public UserLogistica getUsuario() {
        return usuario;
    }

    public LocalDateTime getValidade(){
        return validade;
    }

    public Vacina getVacina() {
        return vacina;
    }



    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setUsuario(UserLogistica usuario) {
        this.usuario = usuario;
    }

    public void setValidade(LocalDateTime validade) {
        this.validade = validade;
    }

    public void setVacina(Vacina vacina) {
        this.vacina = vacina;
    }

    public LocalDateTime getData_descongelamento() {
        return data_descongelamento;
    }


    public void setData_descongelamento(LocalDateTime data_descongelamento) {
        this.data_descongelamento = data_descongelamento;
    }

}
