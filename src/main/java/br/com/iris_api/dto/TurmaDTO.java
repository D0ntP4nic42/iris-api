package br.com.iris_api.dto;

import br.com.iris_api.entity.Professor;

public record TurmaDTO(String identificador, Professor professor, String sala, String disciplina) {
}
