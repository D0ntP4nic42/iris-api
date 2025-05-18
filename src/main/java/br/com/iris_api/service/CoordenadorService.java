package br.com.iris_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.ProfessorRepository;

@Service
public class CoordenadorService {
	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	public List<Professor> listarProfessores() {
		return professorRepository.findAll();
	}

	public List<Aluno> listarAlunos() {
		return alunoRepository.findAll();
	}
}
