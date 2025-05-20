package br.com.iris_api.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.iris_api.security.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "professores")
public class Professor extends User {
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "professor_id")
	@JsonBackReference
	private List<Disciplina> disciplinas;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "trilha_id")
	private Trilha trilha;

	public Professor() {}
	
	public Professor(boolean isCoordenador, String nome, String cpf, String senha) {
		super(nome, cpf, senha, isCoordenador ? Role.COORDENADOR.name() : Role.PROFESSOR.name(), true); //expressão ternária, caso isCoordenador seja true, o role será "COORDENADOR", senão, será "PROFESSOR"
		this.disciplinas = new ArrayList<>();
	}
	
	public void addDisciplina(Disciplina disciplina) {
		this.disciplinas.add(disciplina);
	}
	
	public void removeDisicplina(Disciplina disciplina) {
		this.disciplinas.remove(disciplina);
	}
	
	public void clearDisciplina() {
		this.disciplinas.clear();
	}
	
	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}
}
