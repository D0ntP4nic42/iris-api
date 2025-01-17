package br.com.iris_api.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "professores")
public class Professor extends User {

	@Column(nullable = false)
	private boolean isCoordenador;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "professor_id")
	@JsonBackReference
	private List<Turma> turmas;
	
	public Professor() {}

	public Professor(boolean isCoordenador, String nome, String cpf, String email, String senha) {
		super(nome, cpf, email, senha, "PROFESSOR");
		this.isCoordenador = isCoordenador;
		this.turmas = new ArrayList<>();
	}

	// Getter e Setter
	public boolean isCoordenador() {
		return isCoordenador;
	}

	public void setCoordenador(boolean isCoordenador) {
		this.isCoordenador = isCoordenador;
	}
}
