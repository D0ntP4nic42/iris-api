package br.com.iris_api.controller;

import java.security.Principal;
import java.util.List;

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
import br.com.iris_api.dto.TurmaDTO;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.entity.Turma;
import br.com.iris_api.service.CoordenadorService;
import br.com.iris_api.service.ProfessorService;
import br.com.iris_api.service.TurmaService;
import br.com.iris_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/coordenador")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Coordenador", description = "Operações chamadas pelo coordenador")
public class CoordenadorController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProfessorService professorService;

	@Autowired
	private CoordenadorService coordenadorService;

	@Autowired
	private TurmaService turmaService;

	@Operation(summary = "Listar professores", description = "Retorna a lista de professores cadastrados")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Professores listados com sucesso", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@GetMapping("/professores")
	public ResponseEntity<List<Professor>> listarProfessores() {
		return ResponseEntity.ok().body(coordenadorService.listarProfessores());
	}

	@Operation(summary = "Buscar professor por CPF", description = "Retorna as informações de um professor com base no CPF fornecido")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Professor encontrado com sucesso", content = @Content(schema = @Schema(implementation = Professor.class))),
			@ApiResponse(responseCode = "404", description = "Professor não encontrado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@GetMapping("/professores/{cpf}")
	public ResponseEntity<Professor> infoProfessor(@PathVariable String cpf) {
		var professor = professorService.findByUsername(cpf);

		return ResponseEntity.ok().body(professor);
	}

	@Operation(summary = "Registrar professor", description = "Cadastra um novo professor com base nas informações fornecidas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Professor cadastrado com sucesso", content = @Content()),
			@ApiResponse(responseCode = "400", description = "Professor já cadastrado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@PostMapping("/registrar-professor")
	public ResponseEntity<String> registrarProfessor(@RequestBody ProfessorRegisterDTO professorDTO) {
		professorService.salvar(professorDTO);

		return ResponseEntity.ok().body("Professor cadastrado com sucesso");
	}

	@Operation(summary = "Alterar coordenador", description = "Altera o coordenador atual para o professor com o CPF fornecido")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Coordenador alterado com sucesso", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Professor não encontrado ou Professor logado não encontrado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@PutMapping("/alterar-coordenador")
	public ResponseEntity<String> alterarCoordenador(@PathParam(value = "cpf") String cpf, Principal principal) {

		professorService.alterarCoordenador(cpf, principal.getName());

		return ResponseEntity.ok().body("Coordenador alterado com sucesso");

	}

	@Operation(summary = "Desabilitar/habilitar usuário", description = "DesaBilita/habilita um usuário com base no CPF fornecido")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Usuário desabilitado/habilitado com sucesso", content = @Content()),
			@ApiResponse(responseCode = "400", description = "Não é possível desabilitar um coordenador", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@DeleteMapping("/desabilitar-habilitar-usuario")
	public ResponseEntity<String> desabilitarHabilitarProfessor(@PathParam(value = "cpf") String cpf) {
		userService.desabilitarHabilitar(cpf);

		return ResponseEntity.ok().body("Usuário Desabilitado/habilitado com sucesso");
	}

	@Operation(summary = "Alterar professor", description = "Altera as informações de um professor com base nas informações fornecidas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Professor alterado com sucesso", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Professor não encontrado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@PutMapping("/alterar-professor")
	public ResponseEntity<String> alterarProfessor(@RequestBody ProfessorRegisterDTO professorDTO) {
		professorService.alterar(professorDTO);

		return ResponseEntity.ok().body("Professor alterado com sucesso");

	}

	@Operation(summary = "Listar turmas", description = "Retorna a lista de todas as turmas cadastradas com todas as informações")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Turmas listadas com sucesso", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@GetMapping("/turmas")
	public ResponseEntity<List<Turma>> listarTurmas() {
		return ResponseEntity.ok().body(turmaService.listarTurmasCoordenador());
	}

	@Operation(summary = "Cadastrar turma", description = "Cadastra uma nova turma com base nas informações fornecidas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Turma cadastrada com sucesso", content = @Content()),
			@ApiResponse(responseCode = "400", description = "Erro ao cadastrar turma", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Professor não encontrado", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@PostMapping("/cadastrar-turma")
	public ResponseEntity<String> cadastrarTurma(@RequestBody TurmaDTO turmaDTO) {
		var turma = turmaService.cadastrarTurma(turmaDTO);

		if (turma == null) {
			return ResponseEntity.badRequest().body("Erro ao cadastrar turma");
		}

		professorService.adicionarTurma(turma.getProfessor(), turma);

		return ResponseEntity.ok().body("Turma cadastrada com sucesso");

	}

	@Operation(summary = "Remover turma", description = "Remove uma turma com base no identificador fornecido")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Turma removida com sucesso", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Turma não encontrada", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Um erro desconhecido ocorreu", content = @Content()) })
	@DeleteMapping("/deletar-turma")
	public ResponseEntity<String> deletarTurma(@PathParam(value = "identificador") String identificador) {
		turmaService.deletarTurma(identificador);
		return ResponseEntity.ok().body("Turma removida com sucesso");
	}
}
