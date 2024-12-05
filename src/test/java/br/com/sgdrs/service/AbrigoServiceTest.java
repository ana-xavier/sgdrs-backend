package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.IncluirAbrigoRequest;
import br.com.sgdrs.controller.response.AbrigoResponse;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.domain.Abrigo;
import br.com.sgdrs.domain.Endereco;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.factories.*;
import br.com.sgdrs.repository.AbrigoRepository;
import br.com.sgdrs.repository.EnderecoRepository;
import br.com.sgdrs.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbrigoServiceTest {
    @InjectMocks
    private AbrigoService abrigoService;

    @Mock
    private AbrigoRepository abrigoRepository;

    @Mock
    private EnderecoRepository enderecoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Captor
    ArgumentCaptor<Abrigo> abrigoCaptor;

    @Test
    @DisplayName("Deve retornar lista de abrigos ao listar todos os abrigos")
    public void deveRetornarListaAbrigosAoListarAbrigos(){
        Abrigo abrigo1 = AbrigoFactory.getAbrigo();
        Abrigo abrigo2 = AbrigoFactory.getAbrigo();
        Abrigo abrigo3 = AbrigoFactory.getAbrigo();

        List<Abrigo> lista = List.of(abrigo1, abrigo2, abrigo3);

        when(abrigoRepository.findAll()).thenReturn(lista);

        List<AbrigoResponse> response = abrigoService.listar();

        assertEquals(lista.size(), response.size());
    }


    @Test
    @DisplayName("Deve retornar erro ao criar abrigo com endereço não único")
    public void deveRetornarErroAoCriarAbrigoComEnderecoNaoUnico(){
        IncluirAbrigoRequest request = IncluirAbrigoRequestFactory.getRequest();
        Endereco endereco = EnderecoFactory.getEndereco();

        when(enderecoRepository
                .findUniqueAddress(endereco.getCep(),
                        endereco.getLogradouro(),
                        endereco.getNumero(),
                        endereco.getCidade(),
                        endereco.getEstado()))
                .thenReturn(Optional.of(endereco));

        assertThrows(ResponseStatusException.class, () -> abrigoService.criar(request));
    }

    @Test
    @DisplayName("Deve retornar id do abrigo ao criar abrigo")
    public void deveRetornarIdAbrigoAoCriarAbrigo(){
        IncluirAbrigoRequest request = IncluirAbrigoRequestFactory.getRequest();
        Endereco endereco = EnderecoFactory.getEndereco();

        when(enderecoRepository
                .findUniqueAddress(endereco.getCep(),
                        endereco.getLogradouro(),
                        endereco.getNumero(),
                        endereco.getCidade(),
                        endereco.getEstado()))
                .thenReturn(Optional.empty());


        IdResponse response = abrigoService.criar(request);

        verify(abrigoRepository).save(abrigoCaptor.capture());
        Abrigo abrigo = abrigoCaptor.getValue();

        assertEquals(abrigo.getId(), response.getId());
    }
}