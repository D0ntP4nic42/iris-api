package br.com.iris_api.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "periodoInscricao")
@Entity
public class PeriodoInscricao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "data_fim")
	private LocalDate dataFim;

	@OneToMany(mappedBy = "periodoInscricao")
    private List<Itinerario> itinerarios = new ArrayList<>();

	@ManyToMany
	@JoinTable(name = "periodoInscricao_turma", joinColumns = @JoinColumn(name = "periodoInscricao_id"), inverseJoinColumns = @JoinColumn(name = "turma_id"))
	private List<Turma> turmasPermitidas = new ArrayList<>();

	public PeriodoInscricao() {
		super();
	}

	public PeriodoInscricao(List<Itinerario> itinerarios, List<Turma> turmasPermitidas, LocalDate dataFim) {
		super();
		this.itinerarios = itinerarios;
		this.turmasPermitidas = turmasPermitidas;
		this.dataFim = dataFim;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public List<Itinerario> getItinerarios() {
		return itinerarios;
	}

	public void setItinerarios(List<Itinerario> itinerarios) {
		this.itinerarios = itinerarios;
	}

	public List<Turma> getTurmasPermitidas() {
		return turmasPermitidas;
	}

	public void setTurmasPermitidas(List<Turma> turmasPermitidas) {
		this.turmasPermitidas = turmasPermitidas;
	}

	
}
