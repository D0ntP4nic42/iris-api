package br.com.iris_api.repository;

import br.com.iris_api.entity.Professor;
import br.com.iris_api.entity.Trilha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrilhaRepository extends JpaRepository<Trilha,Long> {

    public Optional<Trilha> findByIdentificador(String identificador);
    public Optional<Trilha> deleteTrilhaByIdentificador(String identificador);
    public Optional<Trilha> findByProfessorId(Professor professor);
}
