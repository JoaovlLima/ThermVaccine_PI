package com.thermvaccine.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;
import com.thermvaccine.model.DataLogger;
import com.thermvaccine.model.HistoricoCaixa;
import com.thermvaccine.model.Lote_coman;
import com.thermvaccine.repository.CaixaRepository;
import com.thermvaccine.repository.ComandaRepository;
import com.thermvaccine.repository.DataLoggerRepository;
import com.thermvaccine.repository.HistoricoCaixaRepository;

public class CaixaService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CaixaRepository caixaRepository;
    private final HistoricoCaixaRepository historicoCaixaRepository;
    private final DataLoggerService dataLoggerService;
    private final ComandaRepository comandaRepository;
    private final ComandaService comandaService;

    public CaixaService() {
        this.caixaRepository = new CaixaRepository();
        this.historicoCaixaRepository = new HistoricoCaixaRepository();
        this.dataLoggerService = new DataLoggerService();
        this.comandaRepository = new ComandaRepository();
        this.comandaService = new ComandaService();
    }

    public void exibirComanda(Caixa caixa) {

        List<Comanda> comandas = comandaRepository.comandasPorIdCaixa(caixa.getId());
        for (Comanda comanda : comandas) {
            System.out.printf("ID: %s\n", comanda.getId());
            System.out.printf("Data de emissao: %s\n", comanda.getData_emissao().format(FORMATTER));
            System.out.printf("Status: %s\n", comanda.getStatus());
            System.out.printf("Local de entrega: CEP - %s | Numero da residencia - %d\n", comanda.getCep(),
                    comanda.getNumEndereco());

        }
    }

    public void criarCaixa(int qtd_max_vac) {

        try {

            List<Caixa> caixasDb = caixaRepository.listar();

            Caixa caixaNova = new Caixa(qtd_max_vac);

            caixasDb.add(caixaNova);

            caixaRepository.salvar(caixasDb);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar Caixa: " + e);
        }
    }

    public void salvarTransporteCaixa(List<Caixa> caixas){

        for (Caixa caixa : caixas) {
            caixaRepository.editar(caixa);
        }
    }

    public List<Caixa> listarCaixasDisponiveis() {

        try {
            List<Caixa> caixasDb = caixaRepository.listar();

            if (caixasDb.isEmpty()) {
                System.out.println("Não há caixas cadastradas");
                return List.of();
            }
            List<Caixa> caixasDisponiveis = new ArrayList<>();

            for (Caixa caixa : caixasDb) {
               
                if (caixa.getDisponivel()) {
                    caixasDisponiveis.add(caixa);
                }
            }

            if (caixasDisponiveis.isEmpty()) {
                System.out.println("Não há caixas disponiveis");
                return List.of();
            }

            return caixasDisponiveis;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar caixas disponiveis" + e);
        }

    }

    // Revisar Associar as comandas a suas devidas caixas
    public List<Caixa> acharCaixas(List<Comanda> comandas) {

        int qtdMax = comandaService.qtdTotalComanda(comandas);

        List<Caixa> caixaDb = listarCaixasDisponiveis();
        List<Caixa> caixasEscolhidas = new ArrayList<>();
        int capTotal = 0;

        for (Caixa caixa : caixaDb) {
            int capRestante = caixa.getQtd_max_vac();

            for (Comanda comanda : comandas) {
                int qtdComanda = comandaService.qtdTotalComanda(List.of(comanda));

                if (comanda.getIdCaixa() == null && capRestante >= qtdComanda) {
                    comanda.setIdCaixa(caixa.getId());
                    capRestante -= qtdComanda;
                }
                
            }

            caixasEscolhidas.add(caixa);
            capTotal += caixa.getQtd_max_vac();

            if (capTotal >= qtdMax) {
                break;
            }
        }

        return caixasEscolhidas;
    }

    public void inserirComandas(Caixa caixa, List<Comanda> comandas) {

        if (!caixa.getDisponivel()) {
            System.out.println("Caixa indisponivel");
            return;
        }

        // Logica para saber se a caixa suporta a qtd
        int qtdTotal = 0;
        for (Comanda comanda : comandas) {
            for (Lote_coman lote_coman : comanda.getLote_coman()) {

                qtdTotal += lote_coman.getQtd();

            }

        }

        if (qtdTotal > caixa.getQtd_max_vac()) {
            System.out.println("Caixa não suporta quantidade de vacina");
            return;
        }

        // Inserindo id caixa nas comandas
        for (Comanda comanda : comandas) {
            comanda.setIdCaixa(caixa.getId());
        }

        caixaRepository.editar(caixa);

    }

    public List<HistoricoCaixa> vincularDatalogger(List<Caixa> caixas) {
        List<DataLogger> dataLoggers = dataLoggerService.dataLoggersDisponiveis();

        if (dataLoggers.size() < caixas.size()) {
            throw new RuntimeException("Dataloggers insuficientes");
        }
        List<HistoricoCaixa> historicoCaixaDb = historicoCaixaRepository.listar();

        for (int i = 0; i < caixas.size(); i++) {

            HistoricoCaixa historicoCaixaNovo = new HistoricoCaixa(dataLoggers.get(i), caixas.get(i), "abcd");
            historicoCaixaDb.add(historicoCaixaNovo);

            dataLoggers.get(i).setDisponivel(false);
            dataLoggerService.editarDatalogger(dataLoggers.get(i));

        }

        return historicoCaixaDb;

    }

    public void salvarVinculoDataCaixa(List<HistoricoCaixa> historicos){

        for (HistoricoCaixa historico : historicos) {
            
            historicoCaixaRepository.editar(historico);
        }
    }

    public int qtdCaixaTransportePlaca(String placa){

        return caixaRepository.caixasPorPlacaTransporte(placa).size();


    }

}
