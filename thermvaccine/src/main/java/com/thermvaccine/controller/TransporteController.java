package com.thermvaccine.controller;

import com.thermvaccine.service.TransporteService;

public class TransporteController {
    
    private final TransporteService transporteService;

    public TransporteController(){
        this.transporteService = new TransporteService();
    }
}
