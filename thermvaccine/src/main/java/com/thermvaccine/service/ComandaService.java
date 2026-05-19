package com.thermvaccine.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.thermvaccine.model.Comanda;
import com.thermvaccine.model.Lote;
import com.thermvaccine.model.Lote_coman;
import com.thermvaccine.model.Vacina;

public class ComandaService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void exibirDados(Comanda comanda){
        System.out.printf("ID: %d\n", comanda.getId());
        System.out.printf("Data de emissao: %s\n", comanda.getData_emissao().format(FORMATTER));
        System.out.printf("Status: %s\n", comanda.getStatus());
        System.out.printf("Local de entrega: CEP - %s | Numero da residencia - %d\n", comanda.getCep(), comanda.getNumEndereco());
        System.out.printf("Referente ao lote: %d", comanda.getLote_coman());

    }

    public Comanda criarComanda(String cep, int numEndereco,List<Lote_coman> lote){

        List<Lote_coman> lotesAtt = calcularMrnaDisponivel(lote);

        return new Comanda(cep, numEndereco, lotesAtt);
        //no View, guardar a nova Comanda numa lista para dps mandar tudo para a Caixa
    }
    
    //criar Comanda criacaoComandas(): while ate n querer mais cadastrar. apenas p ir coletando os atributos e retornar uma List<Comanda> pra consumir no Caixa

public static List<Lote_coman> calcularMrnaDisponivel(List<Lote_coman> lote_comans){

    for (Lote_coman lote_coman : lote_comans) {
        
        Vacina vacina = lote_coman.getLote().getVacina();
        LocalDateTime data_descon = lote_coman.getLote().getData_descongelamento();

        double MRNA_Disponivel = CalculoVidaUtilService.calcularMRNADisponivel(vacina, data_descon);

        System.out.println("LOTE : "+lote_coman.getLote().getId());
        System.out.println("MRNA Disponivel: "+MRNA_Disponivel);
        lote_coman.setMRNA_Disponivel(MRNA_Disponivel);

    }

    return lote_comans;

}


    
}




