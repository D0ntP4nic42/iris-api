package br.com.iris_api.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.TurmaDTO;
import br.com.iris_api.entity.Turma;
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

    public List<Turma> listarTurmasCoordenador(){
        return turmaRepository.findAll();
    }
    
    public Optional<Turma> findByIdentificador(String identificador){
        return turmaRepository.findByIdentificador(identificador);
    }
    
    public Turma cadastrarTurma(TurmaDTO turmaDTO) {

		var professor = professorRepository.findByCpf(turmaDTO.professorCPF());

		if (professor.isEmpty()) {
			throw new EntityNotFoundException("Professor não encontrado");
		} else {

			var professorTurma = professor.get();

			var turma = new Turma(turmaDTO.identificador(), turmaDTO.sala(), turmaDTO.disciplina(), professorTurma);
			return turmaRepository.save(turma);

		}

	}

    public void deletarTurma(String identificador){
        var turma = turmaRepository.findByIdentificador(identificador).orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
//        for (Aluno aluno : turma.getAlunos()) {
//			aluno.getTurmas().remove(turma);
//			alunoRepository.save(aluno);
//		}
        var professor = turma.getProfessor();
        if (professor != null) {
			professor.getTurmas().remove(turma);
		}
        
        turma.getAlunos().clear();
        
        turmaRepository.delete(turma);
    }
}
