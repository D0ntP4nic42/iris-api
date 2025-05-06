package br.com.iris_api.controller;

import br.com.iris_api.entity.Turma;
import br.com.iris_api.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/turma")
public class TurmaController {

    private static final String RESPONSE_FIELD_NAME = "mensagem";
    @Autowired
    private TurmaService turmaService;

    @GetMapping
    public ResponseEntity<Object> listarTurmas() {
        try {
            var response = turmaService.listarTurmas();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Erro ao listar professores"));
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deletarTurma(String identificador) {
        try {
            if(turmaService.findByIdentificador(identificador).isEmpty()) {
                return ResponseEntity.badRequest().
                        body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Turma não encontrada"));
            }
            turmaService.deletarTurma(identificador);
            return ResponseEntity.ok().body(Collections.singletonMap(RESPONSE_FIELD_NAME, "Turma removida com sucesso"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Collections.singletonMap(RESPONSE_FIELD_NAME, e.getMessage()));
        }
    }

}
