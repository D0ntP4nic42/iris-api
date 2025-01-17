package br.com.iris_api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "turmas")
public class Turma {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String identificador;
	
	@Column
	private String sala;
	
	@Column
	private String disciplina;
	
	@Column
	private String horario;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "professor_id")
	@JsonManagedReference 
	private Professor professor;
}
