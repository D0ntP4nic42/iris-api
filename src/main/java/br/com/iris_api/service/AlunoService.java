package br.com.iris_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.AlunoDTO;
import br.com.iris_api.entity.Aluno;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.TurmaRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class AlunoService {
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	@Autowired
	private AlunoRepository alunoRepository;

	@Autowired
	private TurmaRepository turmaRepository;

	public void salvarAluno(AlunoDTO alunoDTO) {
		if (alunoRepository.findByCpf(alunoDTO.cpf()).isPresent()) {
			throw new EntityExistsException("CPF já cadastrado");
		}
		var aluno = new Aluno(alunoDTO.nome(), alunoDTO.cpf(), PASSWORD_ENCODER.encode(alunoDTO.senha()));
		
		var turma = turmaRepository.findByIdentificador(alunoDTO.turmaIdentificador())
				.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
		
		aluno.setTurma(turma);
		alunoRepository.save(aluno);
	}

	public void matricularAluno(String cpf, String identificador) {
		var aluno = alunoRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));
		var turma = turmaRepository.findByIdentificador(identificador)
				.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
		turma.getAlunos().add(aluno);
		aluno.setTurma(turma);
		alunoRepository.save(aluno);
		turmaRepository.save(turma);
	}

}

