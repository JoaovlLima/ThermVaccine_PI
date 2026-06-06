package com.thermvaccine.service;

import java.util.List;
import com.thermvaccine.model.Vacina;
import com.thermvaccine.repository.VacinaRepository;


public class VacinaService {
    private final VacinaRepository vacinaRepository;
    public VacinaService(){
        this.vacinaRepository = new VacinaRepository();
    }

    public void exibirDados(Vacina vacina){
        //System.out.printf("ID: %d\n", this.vacina.getId());
        System.out.printf("Nome: %s\n", vacina.getNome());
        System.out.printf("Temperatura mínima suportada: %f\n", vacina.getTempe_min());
        System.out.printf("Temperatura máxima suportada: %f\n", vacina.getTempe_max());

    }

    //CREATE
    public void criar(Vacina vacina){

        List<Vacina> vacinas = vacinaRepository.listar();

        vacinas.add(vacina);

        vacinaRepository.salvar(vacinas);
    }

    //READ
    public List<Vacina> listar(){

        List<Vacina> vacinas = vacinaRepository.listar();

       return vacinas;

    }

    //UPDATE 
    public void atualizar(String id, String novoNome){
        
        List<Vacina> vacinas = vacinaRepository.listar();

        for (Vacina vacina : vacinas){
            if (vacina.getId() == id){
                vacina.setNome(novoNome);
            }
        }
        vacinaRepository.salvar(vacinas);
    }

    //DELETE
    public void deletar(String id){

        List<Vacina> vacinas = vacinaRepository.listar();

        vacinas.removeIf(
            vacina -> vacina.getId() == id
        );

        vacinaRepository.salvar(vacinas);
    }


}



