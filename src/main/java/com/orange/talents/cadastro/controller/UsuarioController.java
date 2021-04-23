package com.orange.talents.cadastro.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.orange.talents.cadastro.model.Endereco;
import com.orange.talents.cadastro.model.Usuario;
import com.orange.talents.cadastro.repository.EnderecoRepository;
import com.orange.talents.cadastro.repository.UsuarioRepository;

@Controller
public class UsuarioController {
	
	@Autowired
	private UsuarioRepository ur;
	
	@Autowired
	private EnderecoRepository er;
	
	@RequestMapping(value="/cadastrarUsuario", method=RequestMethod.GET)
	public String form() {
		return "usuario/formUsuario";
	}
	
	public boolean emailExiste(String email) {
		Usuario usuario = ur.findByEmail(email);
		if(usuario == null) {
			return false;
		}else {
			return true;
		}
	}
	
	public boolean cpfExiste(String cpf) {
		Usuario usuario = ur.findByCpf(cpf);
		if(usuario == null) {
			return false;
		}else {
			return true;
		}
	}
	
	@RequestMapping(value="/cadastrarUsuario", method=RequestMethod.POST)
	public String form(@Valid Usuario usuario, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem","Verifique os Campos!");
			return "redirect:/cadastrarUsuario";
		}
		if(emailExiste(usuario.getEmail())) {
			attributes.addFlashAttribute("mensagem","Email já cadastrado!");
			return "redirect:/cadastrarUsuario";
		}
		if(cpfExiste(usuario.getEmail())) {
			attributes.addFlashAttribute("mensagem","CPF já cadastrado!");
			return "redirect:/cadastrarUsuario";
		}
		ur.save(usuario);
		attributes.addFlashAttribute("mensagem","Usuário Cadastrado com sucesso!");
		return "redirect:/cadastrarUsuario";
	}
	
	@RequestMapping("/usuarios")
	public ModelAndView listaUsuarios() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Usuario> usuarios = ur.findAll();
		mv.addObject("usuarios", usuarios);
		return mv;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ModelAndView detalhesUsuario(@PathVariable("id") long id) {
		Usuario usuario = ur.findById(id);
		ModelAndView mv = new ModelAndView("usuario/detalhesUsuario");
		mv.addObject("usuario", usuario);
		Iterable<Endereco> enderecos = er.findByUsuario(usuario);
		mv.addObject("enderecos", enderecos);
		return mv;
	}
	
	@RequestMapping("/deletar")
	public String deletarUsuario(long id) {
		Usuario usuario = ur.findById(id);
		ur.delete(usuario);
		return "redirect:/usuarios";
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.POST)
	public String detalhesUsuarioPost(@PathVariable("id") long id, @Valid Endereco endereco, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/{id}";
		}
		Usuario usuario = ur.findById(id);
		endereco.setUsuario(usuario);
		er.save(endereco);
		attributes.addFlashAttribute("mensagem", "Endereço adicionado com sucesso!");
		return "redirect:/{id}";
	}
	
	@RequestMapping("deletarEndereco")
	public String deletarEndereco(String codigo) {
		Endereco endereco = er.findByCodigo(codigo);
		er.delete(endereco);
		
		Usuario usuario = endereco.getUsuario();
		long codigoLong = usuario.getId();
		String codigoFinal = "" + codigoLong;
		return "redirect:/" + codigoFinal;
	}
}
