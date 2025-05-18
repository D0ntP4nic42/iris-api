package br.com.iris_api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.iris_api.dto.AlunoDTO;
import br.com.iris_api.service.AlunoService;
import br.com.iris_api.service.TurmaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/aluno")
@Tag(name = "Aluno", description = "Operações chamadas por alunos")
public class AlunoController {
	@Autowired
	private AlunoService alunoService;

	@Autowired
	private TurmaService turmaService;

	@Operation(summary = "Cadastrar novo aluno", description = "Cria um novo aluno com base nas informações fornecidas no corpo da requisição.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Aluno cadastrado com sucesso", content = @Content()),
			@ApiResponse(responseCode = "400", description = "Aluno já existe", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content()) })
	@PostMapping
	public ResponseEntity<String> cadastrarAluno(@RequestBody AlunoDTO alunoDTO) {
		alunoService.salvarAluno(alunoDTO);
		return ResponseEntity.ok().body("Aluno cadastrado com sucesso");
	}

	@Operation(summary = "Matricular aluno em um turma/trilha/oficina", description = "Matrícula um aluno autenticado em uma turma/trilha/oficina com base no identificador fornecido.")
	@SecurityRequirement(name = "bearerAuth")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Aluno matriculado com sucesso", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Entidade não encontrada", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content()) })
	@PostMapping("/matricular")
	public ResponseEntity<String> matricularAluno(@PathParam(value = "identificador") String identificador,
			Principal principal) {
		alunoService.matricularAluno(principal.getName(), identificador);
		return ResponseEntity.ok().body("Aluno matriculado com sucesso");
	}

	@Operation(summary = "Listar disciplinas do aluno", description = "Lista as disciplinas em que o aluno está matriculado.")
	@SecurityRequirement(name = "bearerAuth")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Disciplinas listadas com sucesso", content = @Content()),
			@ApiResponse(responseCode = "404", description = "Entidade não encontrada", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Erro interno", content = @Content()) })
	@GetMapping("/turmas")
	public ResponseEntity<?> listarDisciplinas(Principal principal) {
		return ResponseEntity.ok().body(turmaService.listarDisciplinasAluno(principal.getName()));
	}
}
