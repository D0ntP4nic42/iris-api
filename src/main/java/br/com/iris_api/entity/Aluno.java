package br.com.iris_api.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.iris_api.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "alunos")
public class Aluno extends User {
	@Column(nullable = false, unique = true)
	private String matricula;
	
	@ManyToMany(mappedBy = "alunos")
	private List<Turma> turmas;
	
	public Aluno() {
		super();
	}

	public Aluno(String nome, String cpf, String senha, String matricula) {
		super(nome, cpf, senha, Role.ALUNO.name());
		this.matricula = matricula;
		this.turmas = new ArrayList<>();
	}

	public void addTurma(Turma turma) {
		this.turmas.add(turma);
	}
	
	public void removeTurma(Turma turma) {
		this.turmas.remove(turma);
	}
	
	public void clearTurmas() {
		this.turmas.clear();
	}
	
	//getter e setter
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public List<Turma> getTurmas() {
		return turmas;
	}
}
