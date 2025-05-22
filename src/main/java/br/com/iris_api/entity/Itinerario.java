package br.com.iris_api.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "itinerario")
public class Itinerario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome", unique = true)
	private String nome;

	@Column(name = "tipo")
	private String tipo;

	@Column(name = "quantidade_vagas")
	private int qtdVagas;

	@ManyToOne
	@JoinColumn(name = "periodoInscricao_id", nullable = true)
	private PeriodoInscricao periodoInscricao;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "itinerario_id")
	private List<Disciplina> disciplinas;

	@OneToMany(mappedBy = "itinerario", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<Horario> horarios;

	@ManyToMany(mappedBy = "itinerarios")
	@JsonManagedReference
	private List<Aluno> alunos = new ArrayList<>();

	public Itinerario() {
		super();
	}

	public Itinerario(String nome, String tipo, int qtdVagas) {
		super();
		this.nome = nome;
		this.tipo = tipo;
		this.qtdVagas = qtdVagas;
		this.horarios = new ArrayList<>();
		this.disciplinas = new ArrayList<>();
		this.alunos = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getQtdVagas() {
		return qtdVagas;
	}

	public void setQtdVagas(int qtdVagas) {
		this.qtdVagas = qtdVagas;
	}

	public List<Horario> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horario> horarios) {
		this.horarios = horarios;
	}

	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Aluno> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<Aluno> alunos) {
		this.alunos = alunos;
	}

	public PeriodoInscricao getInscricao() {
		return periodoInscricao;
	}

	public void setInscricao(PeriodoInscricao inscricao) {
		this.periodoInscricao = inscricao;
	}

}
