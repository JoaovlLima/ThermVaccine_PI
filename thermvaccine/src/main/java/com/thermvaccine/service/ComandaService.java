package com.thermvaccine.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.thermvaccine.model.Comanda;
import com.thermvaccine.model.Lote;
import com.thermvaccine.model.Lote_coman;
import com.thermvaccine.model.Vacina;
import com.thermvaccine.model.Comanda.StatusComanda;
import com.thermvaccine.repository.ComandaRepository;

public class ComandaService {


    private final CalculoVidaUtilService calculoVidaUtilService;
    private final ComandaRepository comandaRepository;
    private final LoteService loteService;

    public ComandaService(){
        this.calculoVidaUtilService = new CalculoVidaUtilService();
        this.comandaRepository = new ComandaRepository();
        this.loteService = new LoteService();
    }

    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void exibirDados(Comanda comanda){
        System.out.printf("ID: %d\n", comanda.getId());
        System.out.printf("Data de emissao: %s\n", comanda.getData_emissao().format(FORMATTER));
        System.out.printf("Status: %s\n", comanda.getStatus());
        System.out.printf("Local de entrega: CEP - %s | Numero da residencia - %d\n", comanda.getCep(), comanda.getNumEndereco());
        System.out.printf("Referente ao lote: %d", comanda.getLote_coman());

    }

    public void criarComanda(String cep, int numEndereco,List<Lote_coman> lote){

        List<Lote_coman> lotesAtt = calcularMrnaDisponivel(lote);

        loteService.descontarLote(lote);

        Comanda comanda = new Comanda(cep, numEndereco, lotesAtt);

        comandaRepository.salvarUni(comanda);
        //no View, guardar a nova Comanda numa lista para dps mandar tudo para a Caixa
    }
    
    //criar Comanda criacaoComandas(): while ate n querer mais cadastrar. apenas p ir coletando os atributos e retornar uma List<Comanda> pra consumir no Caixa

public List<Lote_coman> calcularMrnaDisponivel(List<Lote_coman> lote_comans){

    for (Lote_coman lote_coman : lote_comans) {
        
        Vacina vacina = lote_coman.getLote().getVacina();
        LocalDateTime data_descon = lote_coman.getLote().getData_descongelamento();

        double MRNA_Disponivel = calculoVidaUtilService.calcularMRNADisponivel(vacina, data_descon);

        System.out.println("LOTE : "+lote_coman.getLote().getId());
        System.out.println("MRNA Disponivel: "+MRNA_Disponivel);
        lote_coman.setMRNA_Disponivel(MRNA_Disponivel);

    }

    return lote_comans;

}

public List<Comanda> listarComandasDisponiveis(){

    List<Comanda> comandasDb = comandaRepository.listar();
    List<Comanda> comandasDisponiveis = new ArrayList<Comanda>();

    for (Comanda comanda : comandasDb) {
        if(comanda.getStatus() == StatusComanda.EM_AGUARDO){
            comandasDisponiveis.add(comanda);
        }
    }

    return comandasDisponiveis;

    
}

public void editarComandas(List<Comanda> comandas){

    for (Comanda comanda : comandas) {
        comandaRepository.editar(comanda);
    }
  
}

public int qtdTotalComanda(List<Comanda> comandas){
    int totalComanda = 0;
    for (Comanda comanda : comandas) {
        
        for(Lote_coman lote_coman : comanda.getLote_coman()) {
            
            totalComanda+=lote_coman.getQtd();
        }
    }

    return totalComanda;
}


    
}




