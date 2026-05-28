package com.thermvaccine.repository;

import java.util.List;

public interface IRepository<T> {


    void salvar(List<T> t);


    void editar(T t); 
    
    List<T> listar();

    T findById(String id);


}
