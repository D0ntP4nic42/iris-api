package br.com.iris_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.ProfessorRegisterDTO;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.repository.ProfessorRepository;

@Service
public class ProfessorService {
	@Autowired
	private ProfessorRepository professorRepository;

	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	public Optional<Professor> findByUsername(String cpf) {
		return professorRepository.findByCpf(cpf);
	}

	public Professor salvar(ProfessorRegisterDTO professorDTO) {
		if (professorRepository.findByCpf(professorDTO.cpf()).isPresent()) {
			throw new RuntimeException("CPF já cadastrado");
		}

		var professor = new Professor(professorDTO.isCoordenador(), professorDTO.nome(), professorDTO.cpf(),
				professorDTO.email(), PASSWORD_ENCODER.encode(professorDTO.senha()));
		return professorRepository.save(professor);
	}

	public void deletar(String cpf) {
		var professor = professorRepository.findByCpf(cpf);
		if (professor.isPresent()) {
			professorRepository.delete(professor.get());
		}
	}
}
