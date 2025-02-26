package br.com.iris_api.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.com.iris_api.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "professores")
public class Professor extends User {
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "professor_id")
	@JsonBackReference
	private List<Turma> turmas;

	@OneToMany
	@JoinColumn(name = "professor_id")
	@JsonBackReference
	private List<Atividade> atividades;
	
	public Professor() {
	}

	public Professor(boolean isCoordenador, String nome, String cpf, String senha) {
		super(nome, cpf, senha, isCoordenador ? Role.COORDENADOR.name() : Role.PROFESSOR.name()); //expressão ternária, caso isCoordenador seja true, o role será "COORDENADOR", senão, será "PROFESSOR"
		this.turmas = new ArrayList<>();
	}
}
