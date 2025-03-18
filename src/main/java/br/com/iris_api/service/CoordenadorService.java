package br.com.iris_api.service;

import java.util.List;

import br.com.iris_api.dto.TurmaDTO;
import br.com.iris_api.entity.Turma;
import br.com.iris_api.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.entity.Professor;
import br.com.iris_api.repository.ProfessorRepository;

@Service
public class CoordenadorService {
	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private TurmaRepository turmaRepository;
	
	public List<Professor> listarProfessores() {
		return professorRepository.findAll();
	}

	public Turma cadastrarTurma(TurmaDTO turmaDTO){

		var professor = professorRepository.findByCpf(turmaDTO.professorCPF());

		if(professor.isEmpty()) {

			throw new IllegalArgumentException("Professor não encontrado");
		}
		else{

			var professorTurma = professor.get();

			var turma = new Turma(turmaDTO.identificador(), professorTurma, turmaDTO.sala(), turmaDTO.disciplina());
			return turmaRepository.save(turma);

		}

	}
}
