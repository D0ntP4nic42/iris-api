package br.com.iris_api.repository;

import br.com.iris_api.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

    public Optional<Turma> findByIdentificador(String identificador);
}
