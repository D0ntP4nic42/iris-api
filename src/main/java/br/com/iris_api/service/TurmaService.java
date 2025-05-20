package br.com.iris_api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.TurmaDTO;
import br.com.iris_api.dto.TurmaResponseDTO;
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
	
	@Autowired
	private DisciplinaService disciplinaService;

	public List<TurmaFilter> listarDisciplinasProfessor(String cpf) {
	    Professor professor = professorRepository.findByCpf(cpf)
	        .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

	    List<Turma> turmas = turmaRepository.findTurmasByProfessorId(professor.getId());

	    return turmas.stream()
	        .map(turma -> TurmaFilter.fromEntity(turma, professor))
	        .toList();
	}

	public TurmaFilter listarDisciplinasAluno(String cpf) {
		Aluno aluno = alunoRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
		var turmaFilter = TurmaFilter.fromEntity(aluno.getTurma());

		return turmaFilter;
	}

	public List<TurmaResponseDTO> listarTurmas() {
		var turmaResponse = new ArrayList<TurmaResponseDTO>();
		
		var turmas = turmaRepository.findAll();
		
		for (var turma: turmas) {
			Map<String, String> disciplinas = new HashMap<String, String>();
			
			for (var disciplina: turma.getDisciplinas()) {
				disciplinas.put(disciplina.getNome(), disciplina.getProfessor().getNome());
			}
			
			turmaResponse.add(new TurmaResponseDTO(turma.getId(), turma.getIdentificador(), turma.getSala(), disciplinas, turma.getAlunos(), turma.getTemporadaLetiva()));
		}
		
		return turmaResponse;
	}

	public Optional<Turma> findByIdentificador(String identificador) {
		return turmaRepository.findByIdentificador(identificador);
	}

	public Turma cadastrarTurma(TurmaDTO turmaDTO) {
		var turma = new Turma(turmaDTO.identificador(), turmaDTO.sala(), turmaDTO.temporadaLetiva());
		return turmaRepository.save(turma);

	}

	public void deletarTurma(String identificador) {
		var turma = turmaRepository.findByIdentificador(identificador)
				.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
		for (Aluno aluno : turma.getAlunos()) {
			aluno.setTurma(null);
		}
		turmaRepository.delete(turma);
	}

	public void atualizarTurma(TurmaDTO turmaDTO) {
		var turma = turmaRepository.findByIdentificador(turmaDTO.identificador())
				.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

		turma.setSala(turmaDTO.sala());
		turma.setTemporadaLetiva(turmaDTO.temporadaLetiva());

		turma = turmaRepository.save(turma);

		if (turma == null) {
			throw new RuntimeException("Um erro desconhecido ocorreu ao atualizar a turma");
		}
	}
	
	public void adicionarDisciplinas(String identificador, List<String> nomesDsiciplinas) {
		var turma = turmaRepository.findByIdentificador(identificador)
				.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

		for (String nomeDisciplina : nomesDsiciplinas) {
			var disciplina = disciplinaService.buscarDisciplinaPorNome(nomeDisciplina);
			turma.addDisciplina(disciplina);
		}

		turmaRepository.save(turma);
	}
}
