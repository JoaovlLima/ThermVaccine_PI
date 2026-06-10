package com.thermvaccine.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.util.List;

public class ResetService {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private final String BASE = "thermvaccine\\data\\";

    public void resetar() {
        try {
            resetarCaixas();
            resetarDataLoggers();
            resetarTransportes();
            resetarUsuarios();
            resetarVacinas();
            limpar("lote.json");
            limpar("comanda.json");
            limpar("historicoCaixa.json");
            System.out.println("Banco resetado com sucesso!");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao resetar banco: " + e.getMessage(), e);
        }
    }

    private void resetarCaixas() throws Exception {
        List<Object> caixas = List.of(
            mapper.readValue("""
                {"id":"cx1","qtd_max_vac":2000,"disponivel":true,"idTransporte":null}
            """, Object.class),
            mapper.readValue("""
                {"id":"cx2","qtd_max_vac":2000,"disponivel":true,"idTransporte":null}
            """, Object.class),
            mapper.readValue("""
                {"id":"cx3","qtd_max_vac":2000,"disponivel":true,"idTransporte":null}
            """, Object.class)
        );
        salvar("caixa.json", caixas);
    }

    private void resetarDataLoggers() throws Exception {
        List<Object> dls = List.of(
            mapper.readValue("""
                {"id":"DL01","modelo":"ModeloTest","disponivel":true,"registroDatalogger":[]}
            """, Object.class),
            mapper.readValue("""
                {"id":"DL02","modelo":"ModeloTest","disponivel":true,"registroDatalogger":[]}
            """, Object.class),
            mapper.readValue("""
                {"id":"DL03","modelo":"ModeloTest","disponivel":true,"registroDatalogger":[]}
            """, Object.class)
        );
        salvar("dataLogger.json", dls);
    }

    private void resetarTransportes() throws Exception {
        List<Object> transportes = List.of(
            mapper.readValue("""
                {"placa":"213456","capacidade":20,"disponivel":true}
            """, Object.class),
            mapper.readValue("""
                {"placa":"654321","capacidade":15,"disponivel":true}
            """, Object.class)
        );
        salvar("transporte.json", transportes);
    }

    private void resetarUsuarios() throws Exception {
        List<Object> usuarios = List.of(
            mapper.readValue("""
                {"tier":"LOG","id":null,"re":"001","nome":"Carlos Logística","senha":"1234","cargo":"Chefe Logístico","empresa":null}
            """, Object.class),
            mapper.readValue("""
                {"tier":"QUA","id":null,"re":"002","nome":"Ana Qualidade","senha":"1234","cargo":"Analista de Qualidade","empresa":null}
            """, Object.class)
        );
        salvar("usuario.json", usuarios);
    }

    private void resetarVacinas() throws Exception {
        List<Object> vacinas = List.of(
            mapper.readValue("""
                {"id":"001","nome":"Moderna mRNA-1273","tempe_min":-20.0,"tempe_max":8.0,"ea":100000.0,"a":6.04E12,"threshold":70.0}
            """, Object.class)
        );
        salvar("vacina.json", vacinas);
    }

    private void limpar(String arquivo) throws Exception {
        salvar(arquivo, List.of());
    }

    private void salvar(String nomeArquivo, Object dados) throws Exception {
        File arquivo = new File(BASE + nomeArquivo);
        arquivo.getParentFile().mkdirs();
        mapper.writerWithDefaultPrettyPrinter().writeValue(arquivo, dados);
    }


    public static void main(String[] args) {
    new ResetService().resetar();
}
}

