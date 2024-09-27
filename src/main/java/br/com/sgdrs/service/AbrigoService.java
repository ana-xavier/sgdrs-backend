package br.com.sgdrs.service;

import br.com.sgdrs.controller.response.AbrigoResponse;
import br.com.sgdrs.mapper.AbrigoMapper;
import br.com.sgdrs.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {
    @Autowired
    private AbrigoRepository abrigoRepository;


    public List<AbrigoResponse> listar() {
        return abrigoRepository.findAll().stream()
                .map(AbrigoMapper::toResponse)
                .toList();
    }
}
