package br.com.sgdrs.service;

import br.com.sgdrs.controller.request.IncluirUsuarioRequest;
import br.com.sgdrs.controller.response.UsuarioResponse;
import br.com.sgdrs.domain.Usuario;
import br.com.sgdrs.domain.enums.TipoUsuario;
import br.com.sgdrs.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

	@InjectMocks
	private UsuarioService service;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UsuarioRepository usuarioRepository;

	@Test
	void incluiUsuarioAdminTest() {
		Usuario usuario = new Usuario(UUID.randomUUID(), "Joao", "joao@test.com", "pass", true, TipoUsuario.ADMIN, new ArrayList<>());
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		IncluirUsuarioRequest usuarioRequest = new IncluirUsuarioRequest();

		usuarioRequest.setNome(usuario.getNome());
		usuarioRequest.setEmail(usuario.getEmail());
		usuarioRequest.setTipoUsuario(TipoUsuario.ADMIN);

		UsuarioResponse response = service.incluir(usuarioRequest);

		assertEquals(response.getNome(), "Joao");
		assertEquals(response.getEmail(), "joao@test.com");
		assertEquals(response.getTipoUsuario(), TipoUsuario.ADMIN);

	}

	@Test
	void incluiUsuarioSuperAdmin() {
		Usuario usuario = new Usuario(UUID.randomUUID(), "Joao", "joao@test.com", "pass", true, TipoUsuario.SUPERADMIN, new ArrayList<>());
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		IncluirUsuarioRequest usuarioRequest = new IncluirUsuarioRequest();

		usuarioRequest.setNome(usuario.getNome());
		usuarioRequest.setEmail(usuario.getEmail());
		usuarioRequest.setTipoUsuario(TipoUsuario.SUPERADMIN);

		UsuarioResponse response = service.incluir(usuarioRequest);

		assertEquals(response.getNome(), "Joao");
		assertEquals(response.getEmail(), "joao@test.com");
		assertEquals(response.getTipoUsuario(), TipoUsuario.SUPERADMIN);
	}

	@Test
	void incluiUsuarioVoluntario() {
		Usuario usuario = new Usuario(UUID.randomUUID(), "Joao", "joao@test.com", "pass", true, TipoUsuario.VOLUNTARIO, new ArrayList<>());
		Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

		IncluirUsuarioRequest usuarioRequest = new IncluirUsuarioRequest();

		usuarioRequest.setNome(usuario.getNome());
		usuarioRequest.setEmail(usuario.getEmail());
		usuarioRequest.setTipoUsuario(TipoUsuario.VOLUNTARIO);

		UsuarioResponse response = service.incluir(usuarioRequest);

		assertEquals(response.getNome(), "Joao");
		assertEquals(response.getEmail(), "joao@test.com");
		assertEquals(response.getTipoUsuario(), TipoUsuario.VOLUNTARIO);
	}
}