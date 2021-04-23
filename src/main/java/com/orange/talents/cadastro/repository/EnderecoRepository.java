package com.orange.talents.cadastro.repository;

import org.springframework.data.repository.CrudRepository;

import com.orange.talents.cadastro.model.Endereco;
import com.orange.talents.cadastro.model.Usuario;

public interface EnderecoRepository extends CrudRepository<Endereco, String> {

	Iterable<Endereco> findByUsuario(Usuario usuario);
	Endereco findByCodigo(String codigo);
}
