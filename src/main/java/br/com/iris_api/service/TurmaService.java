package br.com.iris_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.TurmaDTO;
import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.entity.Turma;
import br.com.iris_api.filtros.TurmaFilter;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.repository.TurmaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TurmaService {

	@Autowired
	private TurmaRepository turmaRepository;

	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	public List<Turma> listarTurmasProfessor(String cpf) {
		Professor professor = professorRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));
		return professor.getTurmas();
	}

	public List<TurmaFilter> listarTurmasAluno(String cpf) {
		Aluno aluno = alunoRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
		List<TurmaFilter> turmas = aluno.getTurmas().stream()
				.map(turma -> new TurmaFilter(turma.getIdentificador(), turma.getDisciplina(),
						turma.getProfessor().getNome(), turma.getAlunos().stream().map(Aluno::getNome).toList()))
				.toList();
		
		return turmas;
	}

	public List<Turma> listarTurmas() {
		return turmaRepository.findAll();
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
