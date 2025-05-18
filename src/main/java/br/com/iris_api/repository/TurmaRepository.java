package br.com.iris_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.iris_api.entity.Professor;
import br.com.iris_api.entity.Turma;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

	public Optional<Turma> findByIdentificador(String identificador);

	public Optional<Turma> deleteTurmaByIdentificador(String identificador);

	@Query("""
			    SELECT DISTINCT t
			    FROM Turma t
			    JOIN t.disciplinas d
			    WHERE d.professor.id = :professorId
			""")
	List<Turma> findTurmasByProfessorId(Long professorId);
}
