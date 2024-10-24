package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.CentroDistribuicaoRequest;
import br.com.sgdrs.controller.request.EnderecoRequest;
import br.com.sgdrs.controller.response.CentroDistribuicaoResponse;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.domain.CentroDistribuicao;
import br.com.sgdrs.domain.Endereco;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.mapper.CentroDistribuicaoMapper;
import br.com.sgdrs.mapper.EnderecoMapper;
import br.com.sgdrs.mapper.IdMapper;
import br.com.sgdrs.repository.CentroDistribuicaoRepository;
import br.com.sgdrs.repository.EnderecoRepository;

import br.com.sgdrs.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static br.com.sgdrs.domain.enums.TipoUsuario.SUPERADMIN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
public class CentroDistribuicaoService {
    private static final String MENSAGEM_ENDERECO_JA_EXISTENTE = "O endereço informado já é de outro abrigo";
    private static final String MENSAGEM_CRIADOR_INEXISTENTE = "O usuário criador não existe";
    private static final String MENSAGEM_CRIADOR_INVALIDO = "O usuário criador não é um SUPERADMIN";

    @Autowired
    CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;


    public IdResponse criar(CentroDistribuicaoRequest request,UUID idCriador) {
        Optional<Usuario> usuarioCriador = usuarioRepository.findById(idCriador);
        if(usuarioCriador.isEmpty()){
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, MENSAGEM_CRIADOR_INEXISTENTE);
        }

        if(!usuarioCriador.get().getTipo().equals(SUPERADMIN)){
            throw new ResponseStatusException(BAD_REQUEST, MENSAGEM_CRIADOR_INVALIDO);
        }

        EnderecoRequest enderecoRequest = request.getEndereco();
        Optional<Endereco> enderecoBuscado = enderecoRepository
                .findUniqueAddress(enderecoRequest.getCep(),
                    enderecoRequest.getLogradouro(),
                    enderecoRequest.getNumero(),
                    enderecoRequest.getCidade(),
                    enderecoRequest.getEstado());

        if(enderecoBuscado.isPresent()){
            throw new ResponseStatusException(BAD_REQUEST, MENSAGEM_ENDERECO_JA_EXISTENTE);
        }

        Endereco CentroDistribuicaoEndereco = EnderecoMapper.toEntity(request.getEndereco());
        enderecoRepository.save(CentroDistribuicaoEndereco);

        CentroDistribuicao centroDistribuicao = CentroDistribuicaoMapper.toEntity(request);
        centroDistribuicao.setEndereco(CentroDistribuicaoEndereco);

        centroDistribuicaoRepository.save(centroDistribuicao);

        return IdMapper.toResponse(centroDistribuicao.getId());    
    }

    public List<CentroDistribuicaoResponse> listar() {
        return centroDistribuicaoRepository.findAll().stream()
                .map(CentroDistribuicaoMapper::toResponse)
                .toList();
    }
}
