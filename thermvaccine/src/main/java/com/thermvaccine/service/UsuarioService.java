package com.thermvaccine.service;

import javax.naming.AuthenticationException;

import com.thermvaccine.model.Usuario;
import com.thermvaccine.repository.UsuarioRepository;

public class UsuarioService {
    
    public final UsuarioRepository usuarioRepository;

    public UsuarioService(){
        this.usuarioRepository = new UsuarioRepository();
    }



    public Usuario autenticacao(String re, String senha){

        Usuario usuarioEncontrado = usuarioRepository.findByRe(re);

        if(usuarioEncontrado == null){
             throw new IllegalArgumentException("Usuário não encontrado");
        }

        if(!usuarioEncontrado.getSenha().equals(senha)){
            throw new IllegalAccessError("Senha incorreta");
        }
        

        return usuarioEncontrado;


    }
}
