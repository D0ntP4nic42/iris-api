package br.com.iris_api.controller;

import java.security.Principal;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.iris_api.dto.ProfessorRegisterDTO;
import br.com.iris_api.service.ProfessorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/professores")
@SecurityRequirement(name = "bearerAuth")
public class ProfessorController {

	@Autowired
	private ProfessorService professorService;
	
	@GetMapping
	public ResponseEntity listarProfessores() {
		try {
			return ResponseEntity.ok().body(professorService.listar());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Collections.singletonMap("mensagem", "Erro ao listar professores"));
		}
	}
	
	@GetMapping("/{cpf}")
	public ResponseEntity buscarProfessor(@PathVariable String cpf) {
		try {
			var professor = professorService.findByUsername(cpf);
			if (professor.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap("mensagem", "Professor não encontrado"));
			}
			return ResponseEntity.ok().body(professor.get());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Collections.singletonMap("mensagem", "Erro ao buscar professor"));
		}
	}

	@PostMapping("/registrar")
	public ResponseEntity registrarProfessor(@RequestBody ProfessorRegisterDTO professorDTO) {
		try {
			var professor = professorService.findByUsername(professorDTO.cpf());

			if (professor.isPresent()) {
				return ResponseEntity.badRequest().body(Collections.singletonMap("mensagem", "CPF já cadastrado"));
			}

			professorService.salvar(professorDTO);

			return ResponseEntity.ok().body(Collections.singletonMap("mensagem", "Professor cadastrado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("mensagem", "Erro ao registrar professor"));
		}
	}

	@DeleteMapping("/{cpf}")
	public ResponseEntity removerProfessor(@PathVariable String cpf) {
		try {
			if (professorService.findByUsername(cpf).isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap("mensagem", "Professor não encontrado"));
			}
			professorService.deletar(cpf);
			return ResponseEntity.ok().body(Collections.singletonMap("mensagem", "Professor removido com sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Collections.singletonMap("mensagem", "Erro ao remover professor"));
		}
	}

	@PutMapping("/{cpf}")
	public ResponseEntity alterarCoordenador(@PathVariable String cpf, Principal principal) {
		var professorLogado = professorService.findByUsername(principal.getName());

		try {
			if ((!professorLogado.get().getRole().equals("COORDENADOR") || !professorLogado.get().getCpf().equals(cpf))
					&& professorLogado.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap("mensagem", "Usuário não possui autorização"));
			}

			var professor = professorService.findByUsername(cpf);

			if (professor.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap("mensagem", "Professor não encontrado"));
			}

			professorService.alterarCoordenador(professor.get(), professorLogado.get());

			return ResponseEntity.ok().body(Collections.singletonMap("mensagem", "Coordenador alterado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("mensagem", "Erro ao alterar coordenador"));
		}
	}
	
	@PutMapping("/alterar")
	public ResponseEntity alterarProfessor(@RequestBody ProfessorRegisterDTO professorDTO, Principal principal) {
		try {
			var professorLogado = professorService.findByUsername(principal.getName());
			
			if ((!professorLogado.get().getRole().equals("COORDENADOR") || !professorLogado.get().getCpf().equals(professorDTO.cpf()))
					&& professorLogado.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap("mensagem", "Usuário não possui autorização"));
			}

			var professor = professorService.findByUsername(professorDTO.cpf());
			if (professor.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap("mensagem", "Professor não encontrado"));
			}

			professorService.salvar(professorDTO);

			return ResponseEntity.ok().body(Collections.singletonMap("mensagem", "Professor alterado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Collections.singletonMap("mensagem", "Erro ao alterar professor"));
		}
	}
}