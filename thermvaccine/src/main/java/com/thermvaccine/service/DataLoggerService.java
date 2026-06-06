package com.thermvaccine.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.thermvaccine.model.DataLogger;
import com.thermvaccine.model.HistoricoCaixa;
import com.thermvaccine.model.RegistroDatalogger;
import com.thermvaccine.repository.DataLoggerRepository;
import com.thermvaccine.repository.HistoricoCaixaRepository;
import com.thermvaccine.repository.RegistroRepository;

public class DataLoggerService {

    private final RegistroRepository registroRepository;
    private final DataLoggerRepository dataLoggerRepository;
    private final HistoricoCaixaRepository historicoCaixaRepository;
    
    private final String registroComVariacao = "registroComVariacao.csv";
    private final List<String> registroSemVariacao = List.of("registroSemVariacao.csv", "registroSemVariacao2.csv");
    

    public DataLoggerService() {
        this.registroRepository = new RegistroRepository();
        this.dataLoggerRepository = new DataLoggerRepository();
        this.historicoCaixaRepository = new HistoricoCaixaRepository();

    }


    public void criarDataLogger(){
        try {
            
            List<DataLogger> dataLoggersDb = dataLoggerRepository.listar();

            DataLogger dataLoggerNovo = new DataLogger("ModeloTest", List.of());

            dataLoggersDb.add(dataLoggerNovo);

            dataLoggerRepository.salvar(dataLoggersDb);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar DataLogger: "+e);
        }
    }

    public List<DataLogger> dataLoggersDisponiveis(){

        try {
            
            List<DataLogger> dataLoggersDb = dataLoggerRepository.listar();

            List<DataLogger> dataLoggersDiponiveis = new ArrayList<>();

            for (DataLogger dataLogger : dataLoggersDb) {

                if(dataLogger.isDisponivel()){
                    dataLoggersDiponiveis.add(dataLogger);
                }

            }

            return dataLoggersDiponiveis;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar dataLogger disponiveis: "+e);
        }

    }


    public List<DataLogger> dataLoggersEmUso(){
         
            List<DataLogger> dataLoggersDb = dataLoggerRepository.listar();

            List<DataLogger> dataLoggersEmUso = new ArrayList<>();

            for (DataLogger dataLogger : dataLoggersDb) {

                if(!dataLogger.isDisponivel()){
                    dataLoggersEmUso.add(dataLogger);
                }

            }

            return dataLoggersEmUso;
    }

    
    public void iniciarDataLogger(String idDatalogger) {
        
        // ACESSANDO REGISTROS SIMULADOS 
        List<RegistroDatalogger> registros = leituraArquivo(idDatalogger);

        Thread thread = new Thread(() -> {
            
            try {

            DataLogger dataLogger = dataLoggerRepository.findById(idDatalogger);
            
            for (RegistroDatalogger registro : registros) {   
                

                registro.setData_hora(LocalDateTime.now());

                dataLogger.inserirRegistro(registro);
                dataLoggerRepository.editar(dataLogger);
                TimeUnit.SECONDS.sleep(5);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }});
        
        thread.start();

    }

     public void limparRegistros(String idDataLogger){
        dataLoggerRepository.limparRegistros(idDataLogger);
    }


    public DataLogger buscarPorId(String id) {
        return dataLoggerRepository.findById(id);
    }

    // FUNCÕES AUXILIARES

    public List<RegistroDatalogger> leituraArquivo(String idDataLogger) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<RegistroDatalogger> registros = new ArrayList<>();

        String registroUtilizado = null;
        if(idDataLogger.equals("DL01")){
            registroUtilizado = registroComVariacao;
        }else if(idDataLogger.equals("DL02")){
            registroUtilizado = registroSemVariacao.get(0);
        }else{
            registroUtilizado = registroSemVariacao.get(1);
        }

        InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream(registroUtilizado);

        if (input == null) {
            throw new RuntimeException("Arquivo CSV não encontrado");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String linha;
            br.readLine();

            while ((linha = br.readLine()) != null) {

                String[] valores = linha.split(";");

                // LocalDateTime data_hora;
                // try {
                // data_hora = LocalDateTime.parse(valores[1], formatter);
                // } catch (DateTimeParseException e) {
                // data_hora = corrigirDataHora(valores[1]);
                // }

                float t1 = Float.parseFloat(valores[2]);
                float t2 = Float.parseFloat(valores[3]);
                float temperatura = (t1 + t2) / 2;
                float energia = Float.parseFloat(valores[4]);
                boolean rede = Integer.parseInt(valores[5]) == 1;
                boolean alarme = Integer.parseInt(valores[7]) == 1;
                boolean compressor = Integer.parseInt(valores[8]) == 1;

                RegistroDatalogger registro = new RegistroDatalogger(temperatura, rede, energia, compressor,
                        alarme);

                registros.add(registro);

            }
            // Indice;Data_Hora;T1_C;T2_C;Bateria_V;Rede;Porta;Alarme;Compressor;Status ->
            // csv
            // id,temperatura,rede,energia,compressor,alarme,data_hora -> entidade
        } catch (IOException e) {
            e.printStackTrace();

        }

        return registros;
    }

    public LocalDateTime corrigirDataHora(String dataStr) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // separa data e hora
        String[] partesDataHora = dataStr.split(" ");

        String data = partesDataHora[0];
        String hora = partesDataHora[1];

        // separa HH:mm:ss
        String[] partesHora = hora.split(":");

        int horas = Integer.parseInt(partesHora[0]);
        int minutos = Integer.parseInt(partesHora[1]);
        int segundos = Integer.parseInt(partesHora[2]);

        String dataBase = data + " " +
                String.format("%02d:%02d:%02d", horas, minutos, 0);

        LocalDateTime dataHora = LocalDateTime.parse(dataBase, formatter);

        dataHora = dataHora.plusSeconds(segundos);

        return dataHora;
    }
    

    public void test(String idDatalogger){

        DataLogger dataLogger = dataLoggerRepository.findById(idDatalogger);

        List<RegistroDatalogger> reg = dataLogger.getRegistroDatalogger();


    }

    public void editarDatalogger(DataLogger dataLogger){

        dataLoggerRepository.editar(dataLogger);
    }


    public void liberarDlPorCaixa(String idCaixa){

        List<HistoricoCaixa> historicoCaixasDb = historicoCaixaRepository.listar();

        for (HistoricoCaixa historicoCaixa : historicoCaixasDb) {

            if(historicoCaixa.getCaixa().getId().equals(idCaixa)){
            DataLogger datalogger = dataLoggerRepository.findById(historicoCaixa.getDataLogger().getId());
            if (datalogger == null) continue;
            datalogger.setDisponivel(true);
            dataLoggerRepository.editar(datalogger);
            }
            
        }
        
    }

  
        
}
