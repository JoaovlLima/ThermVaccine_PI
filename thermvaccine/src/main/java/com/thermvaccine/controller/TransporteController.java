package com.thermvaccine.controller;

import java.util.List;
import java.util.Scanner;

import com.thermvaccine.model.Transporte;
import com.thermvaccine.service.TransporteService;

public class TransporteController {
    
    private final TransporteService transporteService;

    public TransporteController(){
        this.transporteService = new TransporteService();
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
}
