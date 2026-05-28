package com.thermvaccine.service;

import com.thermvaccine.model.Transporte;
import com.thermvaccine.repository.CaixaRepository;
import com.thermvaccine.repository.RegistroRepository;
import com.thermvaccine.repository.TransporteRepository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.sql.ast.tree.predicate.BooleanExpressionPredicate;

import com.thermvaccine.model.Caixa;

public class TransporteService {

    private final TransporteRepository transporteRepository;
    private final CaixaRepository caixaRepository;
    private final CaixaService caixaService;

    public TransporteService(){
        this.transporteRepository = new TransporteRepository();
        this.caixaRepository = new CaixaRepository();
        this.caixaService = new CaixaService();
    }

    public void exibirDados(Transporte transp){
        System.out.printf("Placa: %s\n", transp.getPlaca());
        System.out.printf("Capacidade: %d\n", transp.getCapacidade());
        System.out.printf("Data Saida: %s\n", transp.getData_saida());
        //System.out.printf("Data Chegada: %s\n", transp.getData_chegada());
    }

    public Transporte criarTransporte(String placa, int capacidade){

        List<Transporte> transportesDb = transporteRepository.listar();
        Transporte transporte = new Transporte(placa, capacidade);
        transportesDb.add(transporte);
        transporteRepository.salvar(transportesDb);
        return transporte;

    }

    public List<Transporte> listarTransportes(){
        
        return transporteRepository.listar();
    }

    public List<Transporte> listarTransportesDisponiveis(){

        List<Transporte> transportesDb = transporteRepository.listar();
        List<Transporte> transportesDisponiveis = new ArrayList<>();
        for (Transporte transporte : transportesDb) {
            if(transporte.getDisponivel() && 
            transporte.getCapacidade() > caixaService.qtdCaixaTransportePlaca(transporte.getPlaca())){
                transportesDisponiveis.add(transporte);
            }
            
        }
        
        return transportesDisponiveis;
    }


    public void mudarStatus(String placa){

        Transporte transporte = transporteRepository.findById(placa);
        if(transporte == null){
            throw new RuntimeException("Transporte Não encontrado");
        }

        Boolean status = transporte.getDisponivel() ? false : true;
        transporte.setDisponivel(status);

        transporteRepository.editar(transporte);
    }

    public void exibirCaixas(Transporte transporte){


        List<Caixa> caixas = caixaRepository.caixasPorPlacaTransporte(transporte.getPlaca());
        for (Caixa caixa : caixas) {
            System.out.printf("ID: %s\n", caixa.getId());
            System.out.printf("Quantidade maxima de Vacina: %s\n", caixa.getQtd_max_vac());
        
        }
        
    }

    public void inserirCaixas(Transporte transporte, List<Caixa> caixas){

        if(caixas.size() > transporte.getCapacidade()){
            System.out.println("O transporte passou do limite");
            return;
        }
    }

}
