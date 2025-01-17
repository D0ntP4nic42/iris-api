package br.com.iris_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import br.com.iris_api.dto.ProfessorRegisterDTO;
import br.com.iris_api.service.ProfessorService;

@Component
public class DataLoader implements ApplicationRunner {
	@Autowired
	private ProfessorService professorService;

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
		var professor = professorService.findByUsername(CPF_ADMIN);
		if (professor.isPresent()) {
			System.out.println("===== Usuário admin já cadastrado =====");
			return;
		}
		professorService.salvar(new ProfessorRegisterDTO(true, NAME_ADMIN, CPF_ADMIN, EMAIL_ADMIN, PASSWORD_ADMIN));
	}
}
