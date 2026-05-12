package com.thermvaccine.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;
import com.thermvaccine.model.Comanda.StatusComanda;
import com.thermvaccine.model.Lote;

public class ComandaService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Scanner s = new Scanner(System.in);

    public void exibirDados(Comanda comanda){
        System.out.printf("ID: %d\n", comanda.getId());
        System.out.printf("Data de emissao: %s\n", comanda.getData_emissao().format(FORMATTER));
        System.out.printf("Status: %s\n", comanda.getStatus());
        System.out.printf("Local de entrega: CEP - %s | Numero da residencia - %d\n", comanda.getCep(), comanda.getNumEndereco());
        System.out.printf("Referente ao lote: %d", comanda.getLote().getId());

    }

    public Comanda criarComanda(String id, String data_emissao, String cep, int numEndereco, String status, Caixa caixa, Lote lote){

        return new Comanda(id, data_emissao, cep, numEndereco, StatusComanda.valueOf(status), lote, caixa);
        //no View, guardar a nova Comanda numa lista para dps mandar tudo para a Caixa
    }
    
    //criar Comanda criacaoComandas(): while ate n querer mais cadastrar. apenas p ir coletando os atributos e retornar uma List<Comanda> pra consumir no Caixa
}
