package com.thermvaccine.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserQualidade extends Usuario{

    public UserQualidade(String re, String nome, String senha, String cargo, Tier tier){
        super(re, nome, senha, cargo, tier);

    }

}
