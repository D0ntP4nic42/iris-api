package br.com.iris_api.filtros;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Disciplina;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.entity.Turma;

public record TurmaFilter(String identificador, String sala, Map<String, String> disciplinas, List<String> nomesAlunos,
		String temporadaLetiva) {
	public static TurmaFilter fromEntity(Turma turma) {
		Map<String, String> disciplinasMap = turma.getDisciplinas().stream()
				.collect(Collectors.toMap(d -> d.getProfessor().getNome(), Disciplina::getNome));

		List<String> nomesAlunos = turma.getAlunos().stream().map(Aluno::getNome).toList();

		return new TurmaFilter(turma.getIdentificador(), turma.getSala(), disciplinasMap, nomesAlunos,
				turma.getTemporadaLetiva());
	}
	
	public static TurmaFilter fromEntity(Turma turma, Professor professor) {
	    Map<String, String> disciplinasMap = turma.getDisciplinas().stream()
	        .filter(d -> d.getProfessor().getId().equals(professor.getId()))
	        .collect(Collectors.toMap(d -> d.getProfessor().getNome(), Disciplina::getNome));

	    List<String> nomesAlunos = turma.getAlunos().stream()
	        .map(Aluno::getNome)
	        .toList();

	    return new TurmaFilter(
	        turma.getIdentificador(),
	        turma.getSala(),
	        disciplinasMap,
	        nomesAlunos,
	        turma.getTemporadaLetiva()
	    );
	}
}
