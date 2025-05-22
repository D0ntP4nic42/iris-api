package br.com.iris_api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.AlunoDTO;
import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Horario;
import br.com.iris_api.entity.Itinerario;
import br.com.iris_api.filtros.ItinerarioFilter;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.ItinerarioRepository;
import br.com.iris_api.repository.TurmaRepository;
import br.com.iris_api.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AlunoService {
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private TurmaRepository turmaRepository;

	@Autowired
	private ItinerarioRepository itinerarioRepository;

	public void salvarAluno(AlunoDTO alunoDTO) {
		if (userRepository.findByCpf(alunoDTO.cpf()).isPresent()) {
			throw new EntityExistsException("CPF já cadastrado");
		}
		var turma = turmaRepository.findByIdentificador(alunoDTO.turmaIdentificador())
				.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

		var aluno = new Aluno(alunoDTO.nome(), alunoDTO.cpf(), PASSWORD_ENCODER.encode(alunoDTO.senha()));

		aluno.setTurma(turma);
		alunoRepository.save(aluno);
	}

	public void matricularAluno(String cpf, String nome) {
		var aluno = alunoRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
		var itinerario = itinerarioRepository.findByNome(nome)
				.orElseThrow(() -> new EntityNotFoundException("Itinerário não encontrado"));
		
		if (itinerario.getInscricao().getDataFim().isBefore(LocalDate.now())) {
			throw new EntityExistsException("Período de inscrição encerrado");
		}
		
		itinerario.getInscricao().getTurmasPermitidas().stream()
				.filter(turma -> turma.getIdentificador().equals(aluno.getTurma().getIdentificador()))
				.findFirst()
				.orElseThrow(() -> new EntityExistsException("Turma não permitida para o itinerário"));

		if (itinerario.getQtdVagas() <= 0) {
			throw new EntityExistsException("Não há vagas disponíveis para o itinerário");
		}

		// Verifica se o aluno já está matriculado no itinerário
		if (aluno.getItinerarios().stream().anyMatch(it -> it.getNome().equals(itinerario.getNome()))) {
			throw new EntityExistsException("Itinerário já cadastrado para o aluno");
		}

		// Verifica conflitos de horários
		horariosConflitam(itinerario, aluno);

		// Adiciona o itinerário ao aluno
		aluno.addItinerario(itinerario);

		// Salva o aluno (o itinerário será atualizado automaticamente)
		alunoRepository.save(aluno);
		itinerario.setQtdVagas(itinerario.getQtdVagas() - 1);
		itinerarioRepository.save(itinerario);
	}

	public Aluno buscarAlunoPorCpf(String cpf) {
		return alunoRepository.findByCpf(cpf).orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
	}

	private boolean horariosConflitam(Itinerario novoItinerario, Aluno aluno) {
		for (Itinerario itinerarioExistente : aluno.getItinerarios()) {
			for (Horario horarioNovo : novoItinerario.getHorarios()) {
				for (Horario horarioExistente : itinerarioExistente.getHorarios()) {
					// Verifica se os dias são os mesmos
					if (horarioNovo.getDiaDaSemana().equals(horarioExistente.getDiaDaSemana())) {
						// Verifica se os intervalos de tempo se sobrepõem
						boolean overlap = horarioNovo.getComeco().isBefore(horarioExistente.getFim())
								&& horarioNovo.getFim().isAfter(horarioExistente.getComeco());
						if (overlap) {
							throw new EntityExistsException("Os horários conflitam com outro itinerário do aluno"); // Conflito
																													// encontrado
						}
					}
				}
			}
		}
		return false; // Sem conflitos
	}

	public List<ItinerarioFilter> listarItinerarios(String cpf) {
		var aluno = alunoRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

		var itinerarios = aluno.getItinerarios();

		if (itinerarios.isEmpty()) {
			return List.of();
		}

		ArrayList<ItinerarioFilter> itinerariosFiltrados = new ArrayList<>();

		for (Itinerario itinerario : itinerarios) {
			var alunos = itinerario.getAlunos().stream().map(Aluno::getNome).toList();

			HashMap<String, String> disciplinas = new HashMap<>();

			for (var disciplina : itinerario.getDisciplinas()) {
				disciplinas.put(disciplina.getNome(), disciplina.getProfessor().getNome());
			}

			itinerariosFiltrados.add(new ItinerarioFilter(itinerario.getNome(), itinerario.getTipo(),
					itinerario.getQtdVagas(), disciplinas, itinerario.getHorarios(), alunos));
		}

		return itinerariosFiltrados;
	}

	public List<String> listarItinerariosDisponiveis(String cpf) {
		var aluno = alunoRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
		var itinerarios = itinerarioRepository.findAll();
		var itinerariosDisponiveis = new ArrayList<String>();

		if (itinerarios.isEmpty()) {
			return List.of();
		}

		for (var itinerario : itinerarios) {
			if (isItinerarioDisponivel(itinerario, aluno)) {
				itinerariosDisponiveis.add(itinerario.getNome());
			}
		}

		return itinerariosDisponiveis;
	}

	public boolean isItinerarioDisponivel(Itinerario itinerario, Aluno aluno) {
		boolean temVaga = itinerario.getQtdVagas() > 0;
		
		boolean dataValidade = itinerario.getInscricao().getDataFim().isAfter(LocalDate.now());

		boolean turmaPermitida = itinerario.getInscricao().getTurmasPermitidas().stream()
				.anyMatch(turma -> turma.getIdentificador().equals(aluno.getTurma().getIdentificador()));

		var horariosConflitos = aluno.getItinerarios().stream()
				.flatMap(itinerarioExistente -> itinerarioExistente.getHorarios().stream())
				.anyMatch(horarioExistente -> itinerario.getHorarios().stream()
						.anyMatch(horarioNovo -> horarioNovo.getDiaDaSemana().equals(horarioExistente.getDiaDaSemana())
								&& horarioNovo.getComeco().isBefore(horarioExistente.getFim())
								&& horarioNovo.getFim().isAfter(horarioExistente.getComeco())));

		return temVaga && dataValidade && turmaPermitida && !horariosConflitos;
	}
}
