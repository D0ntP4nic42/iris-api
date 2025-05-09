package br.com.iris_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.entity.User;
import br.com.iris_api.repository.UserRepository;
import br.com.iris_api.security.Role;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User findByUsername(String cpf) {
		return userRepository.findByCpf(cpf).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
	}
	
	public void desabilitarHabilitar(String cpf) {
		var user = userRepository.findByCpf(cpf).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
		if (user.getRole().equals(Role.COORDENADOR.name())) {
			throw new IllegalArgumentException("Não é possível desabilitar um coordenador");
		}
		user.setEnabled(!user.isEnabled());
		userRepository.save(user);
	}
}
