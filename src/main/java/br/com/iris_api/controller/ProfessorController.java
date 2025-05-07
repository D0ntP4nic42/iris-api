package br.com.iris_api.controller;

import java.security.Principal;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.iris_api.dto.ProfessorRegisterDTO;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/professores")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Professor", description = "Operações chamadas por professores")
public class ProfessorController {

	@Autowired
	private ProfessorService professorService;

	@Operation(summary = "Listar professores", description = "Retorna a lista de professores cadastrados com alguns limites de dados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Professores listados com sucesso", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@GetMapping
	public ResponseEntity<?> listarProfessores() {
		var response = professorService.listar().stream().map(
				professor -> Map.of("nome", professor.getNome(), "role", professor.getRole(), "id", professor.getId()))
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(response);
	}

	@Operation(summary = "Buscar informações do professor logado", description = "Retorna as informações de um professor")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Professor encontrado com sucesso", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Professor não encontrado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@GetMapping("/info-conta")
	public ResponseEntity<Professor> infoConta(Principal principal) {
		var professor = professorService.findByUsername(principal.getName());

		return ResponseEntity.ok().body(professor);
	}

	@Operation(summary = "Alterar informações do professor logado", description = "Altera as informações de um professor")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Professor alterado com sucesso", content = @Content()),
			@ApiResponse(responseCode = "401", description = "Usuário não possui autorização", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@PutMapping("/alterar-conta")
	public ResponseEntity<String> alterarConta(@RequestBody ProfessorRegisterDTO professorDTO, Principal principal) {
		var professorLogado = professorService.findByUsername(principal.getName());

		if (!professorLogado.getCpf().equals(professorDTO.cpf())) {
			throw new BadCredentialsException("Você não pode alterar informações de outro professor");
		}

		var professor = professorService.alterar(professorDTO);

		if (professor == null) {
			throw new RuntimeException("Erro ao alterar professor");
		}

		return ResponseEntity.ok().body("Conta alterado com sucesso");

	}

	@Operation(summary = "Deletar conta do professor logado", description = "Remove a conta do professor logado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Conta removida com sucesso", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Professor não encontrado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@DeleteMapping("/deletar-conta")
	public ResponseEntity<String> deletarConta(Principal principal) {
		professorService.deletar(principal.getName());
		return ResponseEntity.ok().body("Conta removida com sucesso");
	}
}