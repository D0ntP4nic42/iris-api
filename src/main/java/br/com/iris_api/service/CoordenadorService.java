package br.com.iris_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.AlunoDTO;
import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.repository.TurmaRepository;

@Service
public class CoordenadorService {
	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private AlunoRepository alunoRepository;
	
	@Autowired
	private TurmaRepository turmaRepository;
	
	public List<Professor> listarProfessores() {
		return professorRepository.findAll();
	}

	public List<Aluno> listarAlunos() {
		return alunoRepository.findAll();
	}
	
	public void alterarAluno(AlunoDTO alunoDTO) {
		var aluno = alunoRepository.findByCpf(alunoDTO.cpf()).orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
		var turma = turmaRepository.findByIdentificador(alunoDTO.turmaIdentificador()).orElseThrow(() -> new RuntimeException("Turma não encontrada"));
		
		if (aluno.getTurma() == null || !turma.getIdentificador().equals(aluno.getTurma().getIdentificador())) {
			aluno.setTurma(turma);
		}
		
		aluno.setNome(alunoDTO.nome());
		
		alunoRepository.save(aluno);
	}
}
