package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.EnderecoRequest;
import br.com.sgdrs.controller.request.IncluirAbrigoRequest;
import br.com.sgdrs.controller.response.AbrigoResponse;
import br.com.sgdrs.controller.response.IdResponse;
import br.com.sgdrs.domain.Endereco;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.mapper.AbrigoMapper;
import br.com.sgdrs.mapper.EnderecoMapper;
import br.com.sgdrs.mapper.IdMapper;
import br.com.sgdrs.repository.AbrigoRepository;
import br.com.sgdrs.repository.EnderecoRepository;
import br.com.sgdrs.domain.Abrigo;
import br.com.sgdrs.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.sgdrs.domain.enums.TipoUsuario.SUPERADMIN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Service
public class AbrigoService {
    private static final String MENSAGEM_ENDERECO_JA_EXISTENTE = "O endereço informado já é de outro abrigo";
    private static final String MENSAGEM_CRIADOR_INEXISTENTE = "O usuário criador não existe";
    private static final String MENSAGEM_CRIADOR_INVALIDO = "O usuário criador não é um SUPERADMIN";
    @Autowired
    private AbrigoRepository abrigoRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public List<AbrigoResponse> listar() {
        return abrigoRepository.findAll().stream()
                .map(AbrigoMapper::toResponse)
                .toList();
    }

    @Transactional
    public IdResponse criar(IncluirAbrigoRequest request, UUID idCriador) {
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

        Endereco enderecoAbrigo = EnderecoMapper.toEntity(request.getEndereco());
        enderecoRepository.save(enderecoAbrigo);

        Abrigo abrigo = AbrigoMapper.toEntity(request);
        abrigo.setEndereco(enderecoAbrigo);

        abrigoRepository.save(abrigo);

        return IdMapper.toResponse(abrigo.getId());
    }
}
