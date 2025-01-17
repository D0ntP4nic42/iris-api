package br.com.iris_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.iris_api.entity.User;
import br.com.iris_api.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public Optional<User> findByUsername(String cpf) {
		return userRepository.findByCpf(cpf);
	}
}
