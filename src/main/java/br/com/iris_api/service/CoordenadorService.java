package br.com.iris_api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.AlunoDTO;
import br.com.iris_api.dto.ItinerarioDTO;
import br.com.iris_api.dto.PeriodoInscricaoAlterarDTO;
import br.com.iris_api.dto.PeriodoInscricaoDTO;
import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Horario;
import br.com.iris_api.entity.Itinerario;
import br.com.iris_api.entity.PeriodoInscricao;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.entity.Turma;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.DisciplinaRepository;
import br.com.iris_api.repository.ItinerarioRepository;
import br.com.iris_api.repository.PeriodoInscricaoRepository;
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
	
	@Autowired
	private DisciplinaRepository disciplinaRepository;
	
	@Autowired
	private PeriodoInscricaoRepository periodoInscricaoRepository;

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
		if (itinerarioRepository.findByNome(itinerarioDTO.nome()).isPresent()) {
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

	public void alterarItinerario(ItinerarioDTO itinerarioDTO) {
		var itinerario = itinerarioRepository.findByNome(itinerarioDTO.nome())
				.orElseThrow(() -> new EntityNotFoundException("Itinerário não encontrado"));

		itinerario.setTipo(itinerarioDTO.tipo());
		itinerario.setQtdVagas(itinerarioDTO.qtdVagas());

		itinerario.getHorarios().clear();

		for (var horarioDTO : itinerarioDTO.horarios()) {
			var horario = new Horario(horarioDTO.diaDaSemana(), horarioDTO.comeco(), horarioDTO.fim());
			horario.setItinerario(itinerario);
			itinerario.getHorarios().add(horario);
		}
		itinerarioRepository.save(itinerario);
	}
	
	public void removerItinerario(String nome) {
		var itinerario = itinerarioRepository.findByNome(nome)
				.orElseThrow(() -> new EntityNotFoundException("Itinerário não encontrado"));
		itinerarioRepository.delete(itinerario);
	}
	
	public List<Itinerario> listarItinerarios() {
		return itinerarioRepository.findAll();
	}
	
	public void adicionarDisciplinaAoItinerario(String nomeItinerario, List<String> nomeDisciplina) {
		var itinerario = itinerarioRepository.findByNome(nomeItinerario)
				.orElseThrow(() -> new EntityNotFoundException("Itinerário não encontrado"));
		for (String nome : nomeDisciplina) {
			var disciplina = disciplinaRepository.findByNome(nome)
					.orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada"));
			itinerario.getDisciplinas().add(disciplina);
		}
		
		itinerarioRepository.save(itinerario);
	}
	
	public void removerDisciplinaDoItinerario(String nomeItinerario, List<String> nomeDisciplina) {
		var itinerario = itinerarioRepository.findByNome(nomeItinerario)
				.orElseThrow(() -> new EntityNotFoundException("Itinerário não encontrado"));
		for (String nome : nomeDisciplina) {
			var disciplina = disciplinaRepository.findByNome(nome)
					.orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada"));
			itinerario.getDisciplinas().remove(disciplina);
		}
		
		itinerarioRepository.save(itinerario);
	}
	
	public void cadastrarPeriodoInscricao(PeriodoInscricaoDTO periodoInscricaoDTO) {
		var itinerarios = new ArrayList<Itinerario>();
		
		for (var itinerarioNome : periodoInscricaoDTO.itinerariosNome()) {
			var itinerario = itinerarioRepository.findByNome(itinerarioNome)
					.orElseThrow(() -> new EntityNotFoundException("Itinerário não encontrado"));
			
			if(itinerario.getInscricao() != null) {
				throw new IllegalArgumentException("Itinerário já cadastrado em outro período de inscrição");
			}
			
			itinerarios.add(itinerario);
		}
		
		var turmasPermitidas = new ArrayList<Turma>();
		
		for (var turmaPermitidasNome : periodoInscricaoDTO.turmasPermitidasNome()) {
			var turma = turmaRepository.findByIdentificador(turmaPermitidasNome)
					.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
			turmasPermitidas.add(turma);
		}
		
		var periodoInscricao = periodoInscricaoRepository.save(new PeriodoInscricao(itinerarios, turmasPermitidas, periodoInscricaoDTO.dataFim()));
		for (var itinerario : itinerarios) {
			itinerario.setInscricao(periodoInscricao);
			itinerarioRepository.save(itinerario);
		}
	}
	
	public void removerPeriodoInscricao(Long id) {
		var periodoInscricao = periodoInscricaoRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Período de inscrição não encontrado"));
		
		for (var itinerario : periodoInscricao.getItinerarios()) {
			itinerario.setInscricao(null);
		}
		
		periodoInscricaoRepository.delete(periodoInscricao);
	}
	
	public void alterarPeriodoInscricao(PeriodoInscricaoAlterarDTO periodoInscricaoAlterarDTO) {
		var periodoInscricao = periodoInscricaoRepository.findById(periodoInscricaoAlterarDTO.id())
				.orElseThrow(() -> new EntityNotFoundException("Período de inscrição não encontrado"));
		
		periodoInscricao.setDataFim(periodoInscricaoAlterarDTO.dataFim());
		
		var itinerarios = new ArrayList<Itinerario>();
		
		for (var itinerarioNome : periodoInscricaoAlterarDTO.itinerariosNome()) {
			var itinerario = itinerarioRepository.findByNome(itinerarioNome)
					.orElseThrow(() -> new EntityNotFoundException("Itinerário não encontrado"));
			
			if(itinerario.getInscricao() != null) {
				throw new IllegalArgumentException("Itinerário já cadastrado em outro período de inscrição");
			}
			
			itinerarios.add(itinerario);
		}
		
		var turmasPermitidas = new ArrayList<Turma>();
		
		for (var turmaPermitidasNome : periodoInscricaoAlterarDTO.turmasPermitidasNome()) {
			var turma = turmaRepository.findByIdentificador(turmaPermitidasNome)
					.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
			turmasPermitidas.add(turma);
		}
		
		periodoInscricao.setItinerarios(itinerarios);
		periodoInscricao.setTurmasPermitidas(turmasPermitidas);
		
		periodoInscricaoRepository.save(periodoInscricao);
		
		for (var itinerario : periodoInscricao.getItinerarios()) {
			itinerario.setInscricao(periodoInscricao);
			itinerarioRepository.save(itinerario);
		}
	}
	
	public List<PeriodoInscricao> listarPeriodosInscricao() {
		return periodoInscricaoRepository.findAll();
	}
}
