package br.com.sgdrs.service;

import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Endereco;
import br.com.sgdrs.repository.CentroDistribuicaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/* 
@ExtendWith(MockitoExtension.class)
public class CentroDistribuicaoServiceTest {
    @Mock
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @InjectMocks
    private CentroDistribuicaoService centroDistribuicaoService;


    @Test
    void deveCadastrarNovoCentroDistribuicao() {
        CentroDistribuicao centroDistribuicaoExpect = buildCentroDistribuicao();
        when(centroDistribuicaoRepository.save(centroDistribuicaoExpect)).thenReturn(centroDistribuicaoExpect);

        Optional<CentroDistribuicao> centroDistribuicaoResult = centroDistribuicaoService.salvarCentroDistribuicao(centroDistribuicaoExpect);

        assertTrue(centroDistribuicaoResult.isPresent());
        assertEquals(centroDistribuicaoExpect.getId(), centroDistribuicaoResult.get().getId());
    }

    @Test
    void naoDeveSalvarCentroDistribuicaoNulo() {
        CentroDistribuicao centroDistribuicao = null;
        when(centroDistribuicaoRepository.save(centroDistribuicao)).thenReturn(null);

        Optional<CentroDistribuicao> resultado = centroDistribuicaoService.salvarCentroDistribuicao(centroDistribuicao);

        assertTrue(resultado.isEmpty());
        verify(centroDistribuicaoRepository, times(1)).save(centroDistribuicao);
    }

    @Test
    void deveListarCentrosDistribuicao() {
        List<CentroDistribuicao> listaEsperada = Collections.singletonList(buildCentroDistribuicao());

        when(centroDistribuicaoRepository.findAll()).thenReturn(listaEsperada);

        List<CentroDistribuicao> listaResultado = centroDistribuicaoService.listarCentrosDistribuicao();

        assertNotNull(listaResultado);
        assertEquals(1, listaResultado.size());
        assertEquals(listaEsperada, listaResultado);
        verify(centroDistribuicaoRepository, times(1)).findAll();
    }

    @Test
    void deveListarCentrosDistribuicaoVazio() {
        List<CentroDistribuicao> listaVazia = new ArrayList<>();
        when(centroDistribuicaoRepository.findAll()).thenReturn(listaVazia);

        List<CentroDistribuicao> resultado = centroDistribuicaoService.listarCentrosDistribuicao();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(centroDistribuicaoRepository, times(1)).findAll();
    }

    private CentroDistribuicao buildCentroDistribuicao() {
        return CentroDistribuicao.builder()
                .id(UUID.randomUUID())
                .nome("CENTRO-TESTE")
                .endereco(buildEndereco())
                .build();
    }

    private Endereco buildEndereco() {
        return Endereco.builder()
                .id(UUID.randomUUID())
                .bairro("Bairro teste")
                .cep("1234567")
                .cidade("Cidade teste")
                .estado("Estado teste")
                .logradouro("Logradouro teste")
                .numero("001")
                .build();
    }
}
*/