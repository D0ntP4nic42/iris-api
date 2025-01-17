package br.com.iris_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.iris_api.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public Optional<User> findByCpf(String cpf);
}
