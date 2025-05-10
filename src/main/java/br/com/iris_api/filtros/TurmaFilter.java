package br.com.iris_api.filtros;

import java.util.List;

public record TurmaFilter(String identificador, String disciplina, String nomeProfessor,
		List<String> nomesAlunos) {
}
