package br.com.iris_api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.ProfessorRegisterDTO;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.security.Role;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ProfessorService {
	@Autowired
	private ProfessorRepository professorRepository;

	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	public List<Professor> listar() {
		return professorRepository.findAll();
	}

	public Professor findByCpf(String cpf) {
		var professor = professorRepository.findByCpf(cpf)
				.orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));
		return professor;
	}

	public Professor salvar(ProfessorRegisterDTO professorDTO) {
		if (professorRepository.findByCpf(professorDTO.cpf()).isPresent()) {
			throw new EntityExistsException("Professor já cadastrado");
		}

		professorRepository.save(new Professor(false, professorDTO.nome(), professorDTO.cpf(),
				PASSWORD_ENCODER.encode(professorDTO.senha())));
		return professorRepository.findByCpf(professorDTO.cpf())
				.orElseThrow(() -> new RuntimeException("Erro ao cadastrar professor"));
	}

	public Professor alterar(ProfessorRegisterDTO professorDTO) {
		var professor = professorRepository.findByCpf(professorDTO.cpf())
				.orElseThrow(() -> new EntityExistsException("Professor não encontrado"));
		professor.setNome(professorDTO.nome());
		professor.setSenha(PASSWORD_ENCODER.encode(professorDTO.senha()));
		return professorRepository.save(professor);
	}

	public void alterarCoordenador(String cpfNovoCoordenador, String cpfProfessorLogado) {
		var professor = professorRepository.findByCpf(cpfNovoCoordenador)
				.orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

		var professorLogado = professorRepository.findByCpf(cpfProfessorLogado)
				.orElseThrow(() -> new EntityNotFoundException("Professor logado não encontrado"));

		professor.setRole(Role.COORDENADOR.name());
		professorRepository.save(professor);
		professorLogado.setRole(Role.PROFESSOR.name());
		professorRepository.save(professorLogado);
	}
}
