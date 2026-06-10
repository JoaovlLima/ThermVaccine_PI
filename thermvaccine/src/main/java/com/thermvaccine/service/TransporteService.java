package com.thermvaccine.service;

import com.thermvaccine.model.Transporte;
import com.thermvaccine.model.Comanda.StatusComanda;
import com.thermvaccine.repository.CaixaRepository;
import com.thermvaccine.repository.RegistroRepository;
import com.thermvaccine.repository.TransporteRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.sql.ast.tree.predicate.BooleanExpressionPredicate;

import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;

public class TransporteService {

    private final TransporteRepository transporteRepository;
    private final CaixaRepository caixaRepository;
    private final CaixaService caixaService;
    private final ComandaService comandaService;
    private final DataLoggerService dataLoggerService;

    public TransporteService() {
        this.transporteRepository = new TransporteRepository();
        this.caixaRepository = new CaixaRepository();
        this.caixaService = new CaixaService();
        this.comandaService = new ComandaService();
        this.dataLoggerService = new DataLoggerService();
    }

    public void exibirDados(Transporte transp) {
        System.out.printf("Placa: %s\n", transp.getPlaca());
        System.out.printf("Capacidade: %d\n", transp.getCapacidade());
    }

    public Transporte criarTransporte(String placa, int capacidade) {

        List<Transporte> transportesDb = transporteRepository.listar();
        Transporte transporte = new Transporte(placa, capacidade);
        transportesDb.add(transporte);
        transporteRepository.salvar(transportesDb);
        return transporte;

    }

    public List<Transporte> listarTransportes() {

        return transporteRepository.listar();
    }

    public List<Transporte> listarTransportesDisponiveis() {

        List<Transporte> transportesDb = transporteRepository.listar();
        List<Transporte> transportesDisponiveis = new ArrayList<>();
        for (Transporte transporte : transportesDb) {
            if (transporte.getDisponivel() &&
                    transporte.getCapacidade() > caixaService.qtdCaixaTransportePlaca(transporte.getPlaca())) {
                transportesDisponiveis.add(transporte);
            }

        }

        return transportesDisponiveis;
    }

    public List<Transporte> listarTransportesEmUso(){
         List<Transporte> transportesDb = transporteRepository.listar();
        List<Transporte> transportesEmUso = new ArrayList<>();
        for (Transporte transporte : transportesDb) {
            if (!transporte.getDisponivel()) {
                transportesEmUso.add(transporte);
            }
        }

        return transportesEmUso;
    }
    

    public List<Transporte> listarEmTransito() { // Lista transportes em transito - disponivel=false - novo método
        List<Transporte> transportesDb = transporteRepository.listar();
        List<Transporte> emTransito = new ArrayList<>();
        System.out.println("QTD: " + transportesDb.size());
        for (Transporte transporte : transportesDb) {
            if (!transporte.getDisponivel())
                emTransito.add(transporte);
        }
        return emTransito;
    }

    public void mudarStatus(String placa) {

        Transporte transporte = transporteRepository.findById(placa);
        if (transporte == null) {
            throw new RuntimeException("Transporte Não encontrado");
        }

        Boolean status = transporte.getDisponivel() ? false : true;
        transporte.setDisponivel(status);

        transporteRepository.editar(transporte);
    }

    public void exibirCaixas(Transporte transporte) {

        List<Caixa> caixas = caixaRepository.caixasPorPlacaTransporte(transporte.getPlaca());
        for (Caixa caixa : caixas) {
            System.out.printf("ID: %s\n", caixa.getId());
            System.out.printf("Quantidade maxima de Vacina: %s\n", caixa.getQtd_max_vac());

        }

    }

    public void inserirCaixas(Transporte transporte, List<Caixa> caixas) {

        if (caixas.size() > transporte.getCapacidade()) {
            System.out.println("O transporte passou do limite");
            return;
        }
    }

    public void validarFinalizacao(String placa) {
    List<Caixa> caixas = caixaService.caixasPorTransporte(placa);
    for (Caixa caixa : caixas) {
        for (Comanda comanda : comandaService.comandaPorCaixa(caixa.getId())) {
            if (comanda.getStatus() != StatusComanda.ENTREGUE) {
                throw new IllegalStateException("Há comandas não entregues neste transporte.");
            }
        }
    }
}

public void finalizarTransporte(String placa) {
    Transporte transporte = transporteRepository.findById(placa);
    transporte.setDisponivel(true);
    transporteRepository.editar(transporte);
    for (Caixa caixa : caixaService.caixasPorTransporte(placa)) {
        comandaService.removerComandasDaCaixa(caixa.getId());
    }
    caixaService.liberarCaixaPorTransporte(placa);
}


}
