package com.thermvaccine;

import java.util.List;

import com.thermvaccine.controller.CaixaController;
import com.thermvaccine.model.RegistroDatalogger;
import com.thermvaccine.service.CalculoVidaUtilService;
import com.thermvaccine.service.DataLoggerService;

import com.thermvaccine.service.VacinaService;
import com.thermvaccine.model.Lote;
import com.thermvaccine.service.LoteService;

import com.thermvaccine.model.Usuario;
import com.thermvaccine.model.Empresa;
import com.thermvaccine.model.Transporte;
import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;

import com.thermvaccine.service.TransporteService;
import com.thermvaccine.service.CaixaService;

import java.util.ArrayList;


public class Main {

    public static void main(String[] args) throws InterruptedException {

        // CaixaController caixaController = new CaixaController();

        // caixaController.escolhaCaixa();
       
        DataLoggerService dataLoggerService = new DataLoggerService();

        // dataLoggerService.limparBanco();
        
        List<RegistroDatalogger> registrosTest = dataLoggerService.leituraArquivo();

        System.out.println("---------------------");

        dataLoggerService.salvarRegistro(registrosTest,"DL003");

        System.out.println("ESTA COM THREAD");

        

        CalculoVidaUtilService.iniciar();
        


        // System.out.println("---------------------");

        
        // VacinaService vs = new VacinaService();       
        // LoteService ls = new LoteService();


        // Usuario u = new Usuario("abcdef", "Abc", "abc123", "p");
        // Vacina v = new Vacina("teste", 2, 8);
        // Lote l = new Lote(5, "abc", "2027-09-08 22:22:22", u, v);

        // Empresa e = new Empresa("3232323232", "abcx", "qwerty");
        // Transporte t = new Transporte("ewp1234", 5, "2020-02-02 11:11:12");
        // Caixa cx = new Caixa(1, 10);

        // List<Comanda> comandas = new ArrayList<>();
        // Comanda cm = new Comanda("13503120", 1181, l);
        // comandas.add(cm);

        // TransporteService ts = new TransporteService();
        // CaixaService cs = new CaixaService();

        // vs.exibirDados(v);

        // System.out.println("---------------------");

        // ls.exibirDados(l);

        // System.out.println("---------------------");


        // u.setEmpresa(e);
        // System.out.println(u.getEmpresa().getNome());

        // System.out.println("---------------------");
        // ts.exibirDados(t);
        

        // System.out.println("---------------------");

        // t.addUmaCaixa(cx);
        // ts.exibirCaixas(cx);

        // System.out.println("---------------------");

        // cs.exibirComanda(cx, comandas);



    }
}


    
