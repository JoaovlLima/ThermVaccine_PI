package com.thermvaccine.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserLogistica extends Usuario{

    public UserLogistica(String re, String nome, String senha, String cargo, Tier tier){

        super(re, nome, senha, cargo, tier);

    }

}
