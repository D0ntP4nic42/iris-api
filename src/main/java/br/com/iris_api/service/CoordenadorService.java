package br.com.iris_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.AlunoDTO;
import br.com.iris_api.dto.ItinerarioDTO;
import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Horario;
import br.com.iris_api.entity.Itinerario;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.ItinerarioRepository;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.repository.TurmaRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CoordenadorService {
	@Autowired
	private ProfessorRepository professorRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private TurmaRepository turmaRepository;
	
	@Autowired
	private ItinerarioRepository itinerarioRepository;

	public List<Professor> listarProfessores() {
		return professorRepository.findAll();
	}

	public List<Aluno> listarAlunos() {
		return alunoRepository.findAll();
	}

	public void alterarAluno(AlunoDTO alunoDTO) {
		var aluno = alunoRepository.findByCpf(alunoDTO.cpf())
				.orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
		var turma = turmaRepository.findByIdentificador(alunoDTO.turmaIdentificador())
				.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

		if (aluno.getTurma() == null || !turma.getIdentificador().equals(aluno.getTurma().getIdentificador())) {
			aluno.setTurma(turma);
		}

		aluno.setNome(alunoDTO.nome());

		alunoRepository.save(aluno);
	}
	
	public void adicionarItinerario(ItinerarioDTO itinerarioDTO) {
		if(itinerarioRepository.findByNome(itinerarioDTO.nome()).isPresent()) {
			throw new IllegalArgumentException("Itinerário já cadastrado");
		}
		var itinerario = new Itinerario(itinerarioDTO.nome(), itinerarioDTO.tipo(), itinerarioDTO.qtdVagas());
		
		var horarios = new ArrayList<Horario>();
		
		for (var horarioDTO : itinerarioDTO.horarios()) {
			var horario = new Horario(horarioDTO.diaDaSemana(), horarioDTO.comeco(), horarioDTO.fim());
			horario.setItinerario(itinerario);
			horarios.add(horario);
		}
		
		itinerario.setHorarios(horarios);
		itinerarioRepository.save(itinerario);
	}
}
