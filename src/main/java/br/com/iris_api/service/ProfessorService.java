package br.com.iris_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.ProfessorRegisterDTO;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.security.Role;

@Service
public class ProfessorService {
	@Autowired
	private ProfessorRepository professorRepository;

	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	public Iterable<Professor> listar() {
		return professorRepository.findAll();
	}

	public Optional<Professor> findByUsername(String cpf) {
		return professorRepository.findByCpf(cpf);
	}

	public Professor salvar(ProfessorRegisterDTO professorDTO) {
		var professor = new Professor(false, professorDTO.nome(), professorDTO.cpf(), PASSWORD_ENCODER.encode(professorDTO.senha()));
		return professorRepository.save(professor);
	}

	public void deletar(String cpf) {
		var professor = professorRepository.findByCpf(cpf);
		if (professor.isPresent()) {
			professorRepository.delete(professor.get());
		}
	}
	
	public void alterarCoordenador(Professor professor, Professor professorLogado) {
		professor.setRole(Role.COORDENADOR.name());
		professorRepository.save(professor);
		professorLogado.setRole(Role.PROFESSOR.name());
		professorRepository.save(professorLogado);
	}
}
