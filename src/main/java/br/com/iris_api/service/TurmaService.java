package br.com.iris_api.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.TurmaDTO;
import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.entity.Turma;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.repository.TurmaRepository;
import br.com.iris_api.repository.UserRepository;
import br.com.iris_api.security.Role;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TurmaService {

	@Autowired
	private TurmaRepository turmaRepository;

	@Autowired
	private ProfessorRepository professorRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public List<Turma> listarTurmas(String cpf) {
		var user = userRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
		if (user.getRole().equals(Role.PROFESSOR.name())) {
			Professor professor = (Professor) user;
			return professor.getTurmas();
		} else if (user.getRole().equals(Role.ALUNO.name())) {
			Aluno aluno = (Aluno) user;
			return aluno.getTurmas();
		} else {
			return turmaRepository.findAll();
		}
		
	}

	public Optional<Turma> findByIdentificador(String identificador) {
		return turmaRepository.findByIdentificador(identificador);
	}

	public Turma cadastrarTurma(TurmaDTO turmaDTO) {

		var professor = professorRepository.findByCpf(turmaDTO.professorCPF())
				.orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

		var turma = new Turma(turmaDTO.identificador(), turmaDTO.sala(), turmaDTO.disciplina(), professor);
		return turmaRepository.save(turma);

	}
	
	public void deletarTurma(String identificador) {
		var turma = turmaRepository.findByIdentificador(identificador)
				.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
		
		turma.getProfessor().getTurmas().remove(turma);
		
		turmaRepository.delete(turma);
	}
}
