package br.com.iris_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.entity.Professor;
import br.com.iris_api.repository.ProfessorRepository;

@Service
public class CoordenadorService {
	@Autowired
	private ProfessorRepository professorRepository;
	
	public List<Professor> listarProfessores() {
		return professorRepository.findAll();
	}
}
