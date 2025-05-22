package br.com.iris_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.iris_api.entity.Itinerario;

public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

	Optional<Itinerario> findByNome(String nome);
}
