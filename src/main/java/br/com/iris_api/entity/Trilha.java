package br.com.iris_api.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "trilhas")
public class Trilha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private String identificador;

    @OneToOne(mappedBy = "trilha", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Professor professor;

    @OneToMany(mappedBy = "trilha", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Aluno> alunos;

    @Column
    private int vagas;

    public Trilha() {}


    public Trilha(String nome, String identificador, Professor professor, int vagas) {
        this.nome = nome;
        this.identificador = identificador;
        this.alunos = new ArrayList<>();
        this.professor = professor;
        this.vagas = vagas;
    }


    public void addAluno(Aluno aluno) {
        if (alunos.size() < vagas) {
            alunos.add(aluno);
            aluno.setTrilha(this);
        } else {
            throw new IllegalStateException("Número máximo de alunos atingido");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

}