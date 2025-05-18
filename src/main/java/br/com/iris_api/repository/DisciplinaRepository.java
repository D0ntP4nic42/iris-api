package br.com.iris_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.iris_api.entity.Disciplina;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {

	Optional<Disciplina> findByNome(String nome);

}
