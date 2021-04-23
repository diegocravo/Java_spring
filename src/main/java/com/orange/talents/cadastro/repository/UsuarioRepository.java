package com.orange.talents.cadastro.repository;

import org.springframework.data.repository.CrudRepository;

import com.orange.talents.cadastro.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, String>{

	Usuario findById(long id);
	Usuario findByEmail(String email);
	Usuario findByCpf(String CPF);
	
}
