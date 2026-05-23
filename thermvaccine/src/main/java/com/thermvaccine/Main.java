package com.thermvaccine;

import java.util.List;
import java.util.ArrayList;

import com.thermvaccine.model.Caixa;
import com.thermvaccine.model.Comanda;
import com.thermvaccine.model.DataLogger;
import com.thermvaccine.model.Empresa;
import com.thermvaccine.model.Lote;
import com.thermvaccine.model.RegistroDatalogger;
import com.thermvaccine.model.Transporte;
import com.thermvaccine.model.Usuario;
import com.thermvaccine.model.Vacina;

import com.thermvaccine.controller.CaixaController;

import com.thermvaccine.service.CalculoVidaUtilService;
import com.thermvaccine.service.CaixaService;
import com.thermvaccine.service.DataLoggerService;
import com.thermvaccine.service.LoteService;
import com.thermvaccine.service.TransporteService;
import com.thermvaccine.service.VacinaService;


public class Main {

    public static void main(String[] args) throws InterruptedException {

        // ── SERVICES ─────────────────────────────────────────────
        DataLoggerService dataLoggerService = new DataLoggerService();
        VacinaService vacinaService = new VacinaService();

        // ── VACINA ────────────────────────────────────────────────
        Vacina moderna = new Vacina("Moderna mRNA-1273", -20f, 8f, 100000.0, 6.04e12, 95.0);
        vacinaService.criar(moderna);

        // ── DATALOGGER ────────────────────────────────────────────       
        System.out.println("Vacina criada: " + moderna.getNome());
        dataLoggerService.limparRegistros("755e2bb1-0d94-459f-b92b-abdff43879da");

        // ── SIMULAÇÃO VIA CSV ─────────────────────────────────────
        List<RegistroDatalogger> registrosTest = dataLoggerService.leituraArquivo();
        dataLoggerService.salvarRegistro(registrosTest, "755e2bb1-0d94-459f-b92b-abdff43879da");
        System.out.println("ESTA COM THREAD");

        // ── MONITORAMENTO ─────────────────────────────────────────
        // CalculoVidaUtilService.iniciar("755e2bb1-0d94-459f-b92b-abdff43879da", moderna);


        // ── TESTES ANTIGOS ───────────────────────────

        // CaixaController caixaController = new CaixaController();
        // caixaController.escolhaCaixa();

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
        // ls.exibirDados(l);

        // u.setEmpresa(e);
        // System.out.println(u.getEmpresa().getNome());

        // ts.exibirDados(t);
        // t.addUmaCaixa(cx);
        // ts.exibirCaixas(cx);
        // cs.exibirComanda(cx, comandas);


    }
}


    
