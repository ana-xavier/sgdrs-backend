package br.com.sgdrs.controller;

import br.com.sgdrs.controller.response.AbrigoResponse;
import br.com.sgdrs.domain.Abrigo;
import br.com.sgdrs.service.AbrigoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/abrigos")
public class AbrigoController {

    @Autowired
    private AbrigoService abrigoService;

    @GetMapping("/listar")
    @ResponseStatus(OK)
    public List<AbrigoResponse> listar(){
        return abrigoService.listar();
    }
}
