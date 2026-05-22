package com.thermvaccine.repository;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thermvaccine.model.Usuario;

public class UsuarioRepository {

    private final File arquivo = 
            new File("usuario.json");

    private final ObjectMapper mapper = 
            new ObjectMapper();

    // READ FILE
    public List<Usuario> listar() {

        try {

            if (!arquivo.exists()) {
                return new ArrayList<>();
            }

            return mapper.readValue(
                arquivo,
                new TypeReference<List<Usuario>>() {}
            );

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    //SAVE FILE
    public void salvar(List<Usuario> usuarios) {

        try {

            mapper.writerWithDefaultPrettyPrinter()
                .writeValue(arquivo, usuarios);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
