package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.repository.UsuarioRepository;
import br.com.sgdrs.service.users.UsuarioService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	@InjectMocks
	private UsuarioService service;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UsuarioRepository usuarioRepository;

	@Test
	void superAdminIncluiAdminAbrigoTest() {
		Usuario usuarioCriador = new Usuario(UUID.randomUUID(), "Criador", "criador@test.com", "pass", true,
				TipoUsuario.SUPERADMIN, null, null, new ArrayList<>(), new ArrayList<>());

		Usuario usuario = new Usuario(UUID.randomUUID(), "Joao", "joao@test.com", "pass", true,
				TipoUsuario.ADMIN_ABRIGO, null, null, new ArrayList<>(), new ArrayList<>());

		Mockito.when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioCriador));
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		IncluirUsuarioRequest usuarioRequest = new IncluirUsuarioRequest();

		usuarioRequest.setNome(usuario.getNome());
		usuarioRequest.setEmail(usuario.getEmail());
		usuarioRequest.setTipo(TipoUsuario.ADMIN_ABRIGO);

		UsuarioResponse response = service.incluir(usuarioRequest, usuarioCriador.getId());

		assertEquals(response.getNome(), "Joao");
		assertEquals(response.getEmail(), "joao@test.com");
		assertEquals(response.getTipo(), TipoUsuario.ADMIN_ABRIGO);

	}

	@Test
	void adminCentroDistribuicaoIncluiVoluntarioTest() {
		Usuario usuarioCriador = new Usuario(UUID.randomUUID(), "Criador", "criador@test.com", "pass", true,
				TipoUsuario.ADMIN_CD, null, null, new ArrayList<>(), new ArrayList<>());

		Usuario usuario = new Usuario(UUID.randomUUID(), "Joao", "joao@test.com", "pass", true, TipoUsuario.VOLUNTARIO,
				null, null, new ArrayList<>(), new ArrayList<>());

		Mockito.when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioCriador));
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		IncluirUsuarioRequest usuarioRequest = new IncluirUsuarioRequest();

		usuarioRequest.setNome(usuario.getNome());
		usuarioRequest.setEmail(usuario.getEmail());
		usuarioRequest.setTipo(TipoUsuario.VOLUNTARIO);

		UsuarioResponse response = service.incluir(usuarioRequest, usuarioCriador.getId());

		assertEquals(response.getNome(), "Joao");
		assertEquals(response.getEmail(), "joao@test.com");
		assertEquals(response.getTipo(), TipoUsuario.VOLUNTARIO);
	}

	@Test
	void superAdminIncluiUsuarioComEmailExistenteTest() {
		Usuario usuarioCriador = new Usuario(UUID.randomUUID(), "Criador", "criador@test.com", "pass", true,
				TipoUsuario.SUPERADMIN, null, null, new ArrayList<>(), new ArrayList<>());

		Usuario usuario1 = new Usuario(UUID.randomUUID(), "Joao", "joao@test.com", "pass", true,
				TipoUsuario.ADMIN_ABRIGO,
				null, null, new ArrayList<>(), new ArrayList<>());

		Usuario usuario2 = new Usuario(UUID.randomUUID(), "Joao", "joao@test.com", "pass", true,
				TipoUsuario.ADMIN_ABRIGO,
				null, null, new ArrayList<>(), new ArrayList<>());

		Mockito.when(usuarioRepository.findById(any())).thenReturn(Optional.of(usuarioCriador));
		Mockito.when(usuarioRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(usuario1));

		IncluirUsuarioRequest usuarioRequest = new IncluirUsuarioRequest();

		usuarioRequest.setNome(usuario2.getNome());
		usuarioRequest.setEmail(usuario2.getEmail());
		usuarioRequest.setTipo(usuario2.getTipo());

		assertThrows(ResponseStatusException.class, () -> service.incluir(usuarioRequest, usuarioCriador.getId()));
	}
}