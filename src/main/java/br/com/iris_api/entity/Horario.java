package br.com.iris_api.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "horarios")
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private DayOfWeek diaDaSemana;

    private LocalTime comeco;

    private LocalTime fim;

    @ManyToOne
    private Turma turma;

    // Getters e setters
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public DayOfWeek getDiaDaSemana() {
		return diaDaSemana;
	}

	public void setDiaDaSemana(DayOfWeek diaDaSemana) {
		this.diaDaSemana = diaDaSemana;
	}

	public LocalTime getComeco() {
		return comeco;
	}

	public void setComeco(LocalTime comeco) {
		this.comeco = comeco;
	}

	public LocalTime getFim() {
		return fim;
	}

	public void setFim(LocalTime fim) {
		this.fim = fim;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}
}