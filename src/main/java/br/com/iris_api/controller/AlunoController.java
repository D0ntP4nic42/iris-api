package br.com.iris_api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.iris_api.dto.AlunoDTO;
import br.com.iris_api.service.AlunoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/aluno")
@SecurityRequirement(name = "bearerAuth")
public class AlunoController {
	@Autowired
	private AlunoService alunoService;

	@PostMapping
	public ResponseEntity<String> cadastrarAluno(@RequestBody AlunoDTO alunoDTO) {
		try {
			alunoService.salvarAluno(alunoDTO);
			return ResponseEntity.ok().body("Aluno cadastrado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Erro ao cadastrar aluno");
		}
	}

	@PostMapping("/matricular")
	public ResponseEntity<String> matricularAluno(@PathParam(value = "identificador") String identificador,
			Principal principal) {
		try {
			alunoService.matricularAluno(principal.getName(), identificador);
			return ResponseEntity.ok().body("Aluno matriculado com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Erro ao matricular aluno");
		}
	}
}
