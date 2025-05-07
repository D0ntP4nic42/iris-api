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
import br.com.iris_api.dto.TurmaDTO;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.service.CoordenadorService;
import br.com.iris_api.service.ProfessorService;
import br.com.iris_api.service.TurmaService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/coordenador")
@SecurityRequirement(name = "bearerAuth")
public class CoordenadorController {
	private static final String RESPONSE_FIELD_NOME = "mensagem";
	@Autowired
	private ProfessorService professorService;

	@Autowired
	private CoordenadorService coordenadorService;

	@Autowired
	private TurmaService turmaService;

	@GetMapping("/professores")
	public ResponseEntity listarProfessores() {
		try {
			return ResponseEntity.ok().body(coordenadorService.listarProfessores());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao listar professores"));
		}
	}

	@GetMapping("/professores/{cpf}")
	public ResponseEntity infoProfessor(@PathVariable String cpf) {
		try {
			Professor professor = professorService.findByUsername(cpf)
					.orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

			return ResponseEntity.ok().body(professor);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao buscar professor"));
		}
	}

	@PostMapping("/registrar-professor")
	public ResponseEntity registrarProfessor(@RequestBody ProfessorRegisterDTO professorDTO) {
		try {
			professorService.salvar(professorDTO);

			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor cadastrado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao registrar professor"));
		}
	}

	@PutMapping("/alterar-coordenador")
	public ResponseEntity alterarCoordenador(@PathParam(value = "cpf") String cpf, Principal principal) {
		var professorLogado = professorService.findByUsername(principal.getName());

		try {
			var professor = professorService.findByUsername(cpf)
					.orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

			professorService.alterarCoordenador(professor, professorLogado.get());

			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Coordenador alterado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao alterar coordenador"));
		}
	}

	@DeleteMapping("/deletar-professor")
	public ResponseEntity deletarProfessor(@PathParam(value = "cpf") String cpf) {
		try {
			Professor professor = professorService.findByUsername(cpf)
					.orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

			if (professor.getRole().equals("COORDENADOR")) {
				return ResponseEntity.badRequest().body(
						Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro, não é possível deletar o coordenador"));
			}
			professorService.deletar(cpf);
			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor removido com sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao remover professorCPF"));
		}
	}

	@PutMapping("/alterar-professor")
	public ResponseEntity alterarProfessor(@RequestBody ProfessorRegisterDTO professorDTO) {
		try {
			professorService.salvar(professorDTO);

			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor alterado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao alterar professorCPF"));
		}
	}
	
	@GetMapping("/turmas")
	public ResponseEntity listarTurmas() {
		try {
			return ResponseEntity.ok().body(turmaService.listarTurmasCoordenador());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao listar turmas"));
		}
	}

	@PostMapping("/cadastrar-turma")
	public ResponseEntity cadastrarTurma(@RequestBody TurmaDTO turmaDTO) {
		try {
			var professor = professorService.findByUsername(turmaDTO.professorCPF())
					.orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

			var turma = coordenadorService.cadastrarTurma(turmaDTO);

			if (turma == null) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao cadastrar turma"));
			}
			
			professorService.adicionarTurma(professor, turma);

			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Turma cadastrada com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao cadastrar turma"));
		}
	}
	
	@DeleteMapping("/deletar-turma/{identificador}")
	public ResponseEntity deletarTurma(@PathParam(value = "identificador") String identificador) {
		try {
			turmaService.deletarTurma(identificador);
			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Turma removida com sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao remover turma"));
		}
	}
}
