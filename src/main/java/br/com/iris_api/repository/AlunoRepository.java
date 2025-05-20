package br.com.iris_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Turma;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	Optional<Aluno> findByCpf(String cpf);

}
