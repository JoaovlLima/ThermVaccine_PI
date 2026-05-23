package com.thermvaccine.repository;

import java.util.List;

public interface IRepository<T> {

    //void limpar(); //* user e vacina

    void salvar(List<T> t);

    void editar(T t); //* user, vacina e historicoCaixa
    
    List<T> listar();

    T findById(String id);


}
