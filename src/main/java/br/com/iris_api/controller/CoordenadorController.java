package br.com.iris_api.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import br.com.iris_api.dto.TurmaDTO;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.iris_api.dto.ProfessorRegisterDTO;
import br.com.iris_api.service.CoordenadorService;
import br.com.iris_api.service.ProfessorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

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
			Optional<Professor>professor = professorService.findByUsername(cpf);

			if (professor.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor não encontrado"));
			}
			return ResponseEntity.ok().body(professor.get());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao buscar professor"));
		}
	}

	@PostMapping("/registrar-professor")
	public ResponseEntity registrarProfessor(@RequestBody ProfessorRegisterDTO professorDTO) {
		try {
			 var professor = professorService.findByUsername(professorDTO.cpf());

			if (professor.isPresent()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "CPF já cadastrado"));
			}

			professorService.salvar(professorDTO);

			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor cadastrado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao registrar professor"));
		}
	}

	@PutMapping("/alterar-coordenador/{cpf}")
	public ResponseEntity alterarCoordenador(@PathVariable String cpf, Principal principal) {
		var professorLogado = professorService.findByUsername(principal.getName());

		try {
			var professor = professorService.findByUsername(cpf);

			if (professor.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor não encontrado"));
			}

			professorService.alterarCoordenador(professor.get(), professorLogado.get());

			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Coordenador alterado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao alterar coordenador"));
		}
	}

	@DeleteMapping("/deletar-professor/{cpf}")
	public ResponseEntity deletarProfessor(@PathVariable String cpf) {
		try {
			if (professorService.findByUsername(cpf).isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor não encontrado"));
			}
			professorService.deletar(cpf);
			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor removido com sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao remover professor"));
		}
	}

	@PutMapping("/alterar-professor")
	public ResponseEntity alterarProfessor(@RequestBody ProfessorRegisterDTO professorDTO) {
		try {
			var professor = professorService.findByUsername(professorDTO.cpf());
			if (professor.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor não encontrado"));
			}

			professorService.salvar(professorDTO);

			return ResponseEntity.ok()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor alterado com sucesso"));

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao alterar professor"));
		}
	}

	@PostMapping("/cadastrar-turma")
	public ResponseEntity cadastrarTurma(@RequestBody TurmaDTO turmaDTO) {
		try {
			var professor = professorService.findByUsername(turmaDTO.professor().getCpf());

			if (professor.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Professor não encontrado"));
			}

			coordenadorService.cadastrarTurma(turmaDTO);

			var turma = turmaService.listaTurmaPorIdentificador(turmaDTO.identificador());

			if (turma.isEmpty()) {
				return ResponseEntity.badRequest()
						.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao cadastrar turma"));
			}
			else {

                var turmaProfessor = professor.get();
                var turma1 = turma.get();

                professorService.adicionarTurma(turmaProfessor,turma1);

                return ResponseEntity.ok().body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Turma cadastrada com sucesso"));
            }


		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Erro ao cadastrar turma"));
		}

    }

}
