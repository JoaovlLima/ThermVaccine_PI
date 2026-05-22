package com.thermvaccine.model;

public class UserLogistica extends Usuario{

    public UserLogistica(String re, String nome, String senha, String cargo, Tier tier){
        // if(tier != Tier.LOG){
        //     throw new IllegalAccessError("Esse tier não pertence a esse usuario");
        // }
        super(re, nome, senha, cargo, tier);

    }

}
