package br.com.sgdrs.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

	@Test
	void adicionaPermissaoParaUsuario() {

		Usuario user = new Usuario();
		user.setPermissoes(new ArrayList<>());

		Permissao permissao = new Permissao();
		UUID idPermissao = UUID.randomUUID();
		permissao.setId(idPermissao);
		permissao.setFuncao(Funcao.ADMIN);

		user.adicionarPermissao(permissao);

		assertEquals(user.getPermissoes().get(0).getId(), idPermissao);
	}
}