package br.com.iris_api.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.iris_api.security.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "aluno")
public class Aluno extends User {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "turma_id")
	@JsonBackReference
	private Turma turma;
	
	@ManyToMany
    @JoinTable(
        name = "aluno_itinerario",
        joinColumns = @JoinColumn(name = "aluno_id"),
        inverseJoinColumns = @JoinColumn(name = "itinerario_id")
    )
	@JsonBackReference
    private List<Itinerario> itinerarios = new ArrayList<>();


	public Aluno() {
		super();
	}

	public Aluno(String nome, String cpf, String senha) {
		super(nome, cpf, senha, Role.ALUNO.name(), true);
	}

	public void addItinerario(Itinerario itinerario) {
		this.itinerarios.add(itinerario);
	}

	public void removeItinerario(Itinerario itinerario) {
		this.itinerarios.remove(itinerario);
	}

	public List<Itinerario> getItinerarios() {
		return itinerarios;
	}

	// getter e setter
	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

}
