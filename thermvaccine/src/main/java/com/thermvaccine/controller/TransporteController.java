package com.thermvaccine.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.thermvaccine.model.Comanda;
import com.thermvaccine.model.Transporte;
import com.thermvaccine.service.ComandaService;
import com.thermvaccine.service.TransporteService;

public class TransporteController {
    
    private final TransporteService transporteService;
    private final ComandaService comandaService;

    public TransporteController(){
        this.transporteService = new TransporteService();
        this.comandaService = new ComandaService();
        
    }

    public Transporte escolhaTransporte(){
        Scanner sc = new Scanner(System.in);
        
        List<Transporte> transportes = transporteService.listarTransportesDisponiveis();
        if(transportes.size() ==0){
            System.out.println("NÂO TEM TRANSPORTE");
            System.out.println("Deseja Criar seu Transporte?");
            System.out.println("1-)Sim | 2-)Não");
            int ct_choice = sc.nextInt();
            if(ct_choice == 1){
                return criarTransporte();
            }else{
                return null;
            }
            

        }
        System.out.println("Escolhe um transporte: ");
        
        for (int i = 0; i < transportes.size(); i++) {
            
            System.out.println("\n"+(i+1)+") "+transportes.get(i).getPlaca()+" - capacidade: "+transportes.get(i).getCapacidade());
        }

        int t_choice = sc.nextInt() -1;

        System.err.println("Transporte escolhido: "+transportes.get(t_choice).getPlaca());
        return transportes.get(t_choice);
    }

    public Transporte criarTransporte(){
        Scanner sc = new Scanner(System.in);


        System.out.println("Placa :");
        String placa = sc.nextLine();
        System.out.println("Capacidade: ");
        int capacidade = sc.nextInt();

        Transporte transporte = transporteService.criarTransporte(placa, capacidade);
        return transporte;
    }

    public List<Comanda> escolhaComanda(){
        Scanner sc = new Scanner(System.in);
        List<Comanda> comandasEscolhidas = new ArrayList<Comanda>();
        while(true){
        List<Comanda> comandasdb = comandaService.listarComandasDisponiveis();

        System.out.println("Escolha a Comanda para Transporte: ");

        for (int i = 0; i < comandasdb.size(); i++) {
            
            System.out.println((i+1)+"-) "+comandasdb.get(i).getId()+"| emitida: "+comandasdb.get(i).getData_emissao());
        }

        int escolha = sc.nextInt()-1;

        comandasEscolhidas.add(comandasdb.get(escolha));
        comandasdb.remove(escolha);

        System.out.println("Deseja escolher mais uma comanda? 1-SIM | 2-NÃO");
        escolha = sc.nextInt();
        if(escolha == 2){
            break;
        }

    }
        return comandasEscolhidas;
    }
}
