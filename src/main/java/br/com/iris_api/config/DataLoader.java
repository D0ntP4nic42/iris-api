package br.com.iris_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.iris_api.entity.Professor;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.security.Role;

@Component
public class DataLoader implements ApplicationRunner {
	@Autowired
	private ProfessorRepository professorRepository;

	@Value("${user.name.admin}")
	private String NAME_ADMIN;

	@Value("${user.cpf.admin}")
	private String CPF_ADMIN;

	@Value("${user.email.admin}")
	private String EMAIL_ADMIN;

	@Value("${user.password.admin}")
	private String PASSWORD_ADMIN;

	@Override
	public void run(ApplicationArguments args) {
		var professor = professorRepository.findByCpf(CPF_ADMIN);
		if (professor.isPresent() || professorRepository.existsByRole(Role.COORDENADOR.name())) {
			System.out.println("\n\n ===== Usuário admin já cadastrado ou Coordenador já existe ===== \n\n");
			return;
		}
		professorRepository.save(new Professor(true, NAME_ADMIN, CPF_ADMIN, EMAIL_ADMIN, PASSWORD_ADMIN));
		System.out.println("\n\n ===== Usuário admin cadastrado com sucesso ===== \n\n");
	}
}
