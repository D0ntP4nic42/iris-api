package br.com.iris_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.iris_api.entity.Professor;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
	Optional<Professor> findByCpf(String cpf);
}
