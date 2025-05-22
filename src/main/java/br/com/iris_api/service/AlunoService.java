package br.com.iris_api.service;

import br.com.iris_api.entity.Horario;
import br.com.iris_api.entity.Itinerario;
import br.com.iris_api.repository.ItinerarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.AlunoDTO;
import br.com.iris_api.entity.Aluno;
import br.com.iris_api.repository.AlunoRepository;
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
	}
	
	public Aluno buscarAlunoPorCpf(String cpf) {
		return alunoRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
	}


	private boolean horariosConflitam(Itinerario novoItinerario, Aluno aluno) {
		for (Itinerario itinerarioExistente : aluno.getItinerarios()) {
			for (Horario horarioNovo : novoItinerario.getHorarios()) {
				for (Horario horarioExistente : itinerarioExistente.getHorarios()) {
					// Verifica se os dias são os mesmos
					if (horarioNovo.getDiaDaSemana().equals(horarioExistente.getDiaDaSemana())) {
						// Verifica se os intervalos de tempo se sobrepõem
						boolean overlap = horarioNovo.getComeco().isBefore(horarioExistente.getFim()) &&
								horarioNovo.getFim().isAfter(horarioExistente.getComeco());
						if (overlap) {
							throw new EntityExistsException("Os horários conflitam com outro itinerário do aluno"); // Conflito encontrado
						}
					}
				}
			}
		}
		return false; // Sem conflitos
	}

}

