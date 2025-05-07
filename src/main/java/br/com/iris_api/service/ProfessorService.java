package br.com.iris_api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.iris_api.dto.ProfessorRegisterDTO;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.entity.Turma;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.security.Role;
import jakarta.persistence.EntityExistsException;

@Service
public class ProfessorService {
	@Autowired
	private ProfessorRepository professorRepository;

	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	public List<Professor> listar() {
		return professorRepository.findAll();
	}

	public Optional<Professor> findByUsername(String cpf) {
		return professorRepository.findByCpf(cpf);
	}

	public Professor salvar(ProfessorRegisterDTO professorDTO) {
		if (professorRepository.findByCpf(professorDTO.cpf()).isPresent()) {
			throw new EntityExistsException("Professor já cadastrado");
		}

		professorRepository.save(new Professor(false, professorDTO.nome(), professorDTO.cpf(),
				PASSWORD_ENCODER.encode(professorDTO.senha())));
		return professorRepository.findByCpf(professorDTO.cpf())
				.orElseThrow(() -> new EntityExistsException("Erro ao cadastrar professor"));
	}

	public Professor alterar(ProfessorRegisterDTO professorDTO) {
		var professor = professorRepository.findByCpf(professorDTO.cpf())
				.orElseThrow(() -> new EntityExistsException("Professor não encontrado"));
		professor.setNome(professorDTO.nome());
		professor.setSenha(PASSWORD_ENCODER.encode(professorDTO.senha()));
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

	public void adicionarTurma(Professor professor, Turma turma) {
		professor.addTurma(turma);
		professorRepository.save(professor);
	}
}
