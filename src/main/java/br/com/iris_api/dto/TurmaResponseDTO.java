package br.com.iris_api.dto;

import java.util.List;
import java.util.Map;

import br.com.iris_api.entity.Aluno;

public record TurmaResponseDTO (Long id, String identificador, String sala, Map<String, String> disciplinas, List<Aluno> alunos, String temoporadaLetiva) {

}
