package br.com.iris_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import br.com.iris_api.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "alunos")
public class Aluno extends User {
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "turma_id")
	@JsonBackReference
	private Turma turma;

	@ManyToOne
	@JsonBackReference
	private Trilha trilha;

	public Aluno() {
		super();
	}

	public Aluno(String nome, String cpf, String senha) {
		super(nome, cpf, senha, Role.ALUNO.name(), true);
		this.trilha = null;
	}

	// getter e setter
	public void setTrilha(Trilha trilha) {
		this.trilha = trilha;
	}

	public Trilha getTrilha() {
		return trilha;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}

}
