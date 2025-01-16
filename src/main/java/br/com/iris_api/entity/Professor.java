package br.com.iris_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "professores")
public class Professor extends User {

	@Column(nullable = false)
	private boolean isCoordenador;

	public Professor(boolean isCoordenador, String nome, String cpf, String email, String senha) {
		super(nome, cpf, email, senha);
		this.isCoordenador = isCoordenador;
	}
	
	// Getter e Setter
	public boolean isCoordenador() {
		return isCoordenador;
	}

	public void setCoordenador(boolean isCoordenador) {
		this.isCoordenador = isCoordenador;
	}
}
