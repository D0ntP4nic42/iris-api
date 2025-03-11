package br.com.iris_api.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

	private static final String RESPONSE_FIELD_NAME = "mensagem";
	@Autowired
	private ProfessorService professorService;

	@GetMapping
	public ResponseEntity listarProfessores() {
		try {
			var response = professorService.listar().stream().map(professor -> Map.of("nome", professor.getNome(),
					"role", professor.getRole(), "id", professor.getId())).collect(Collectors.toList());
			
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Erro ao listar professores"));
		}
	}

	@GetMapping("/info-conta")
	public ResponseEntity infoConta(Principal principal) {
		try {
			var professor = professorService.findByUsername(principal.getName());

			if (professor.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Professor não encontrado"));
			}
			return ResponseEntity.ok().body(professor.get());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Erro ao buscar professor"));
		}
	}

	@PutMapping("/alterar-conta")
	public ResponseEntity alterarConta(@RequestBody ProfessorRegisterDTO professorDTO, Principal principal) {
		try {
			var professorLogado = professorService.findByUsername(principal.getName());

			if (!professorLogado.get().getCpf().equals(professorDTO.cpf()) && professorLogado.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Usuário não possui autorização"));
			}

			professorService.salvar(professorDTO);

			return ResponseEntity.ok().body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Conta alterado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Erro ao alterar professor"));
		}
	}

	@DeleteMapping("/deletar-conta")
	public ResponseEntity deletarConta(Principal principal) {
		try {
			if (professorService.findByUsername(principal.getName()).isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Professor não encontrado"));
			}
			professorService.deletar(principal.getName());
			return ResponseEntity.ok().body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Conta removida com sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Erro ao remover conta"));
		}
	}
}