package br.com.iris_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.DisciplinaDTO;
import br.com.iris_api.entity.Disciplina;
import br.com.iris_api.repository.DisciplinaRepository;
import br.com.iris_api.repository.ProfessorRepository;

@Service
public class DisciplinaService {
	@Autowired
	private DisciplinaRepository disciplinaRepository;
	
	@Autowired
	private ProfessorService professorService;
	
	@Autowired
	private ProfessorRepository professorRepository;	
	
	public void salvarDisciplina(DisciplinaDTO disciplinaDTO) {
		var professor = professorService.findByCpf(disciplinaDTO.professorCpf());
		disciplinaRepository.save(new Disciplina(disciplinaDTO.nome(), professor));
	}
	
	public void deletarDisciplina(String nome) {
	    var disciplina = disciplinaRepository.findByNome(nome)
	        .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

	    var professor = disciplina.getProfessor(); // salva antes de perder referência

	    // Remove da lista de disciplinas do professor
	    professor.removeDisicplina(disciplina); // remove disciplina internamente
	    professorRepository.save(professor);    // atualiza relação

	    // Agora pode deletar a disciplina sem setar o professor como null
	    disciplinaRepository.delete(disciplina);
	}
	
	public Disciplina buscarDisciplinaPorNome(String nome) {
		var disciplina = disciplinaRepository.findByNome(nome).orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
		return disciplina;
	}

	public List<Disciplina> listarDisciplinas() {
		var disciplinas = disciplinaRepository.findAll();
		return disciplinas;
	}
}
