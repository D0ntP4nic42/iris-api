package br.com.iris_api.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.iris_api.entity.Aluno;
import br.com.iris_api.entity.Disciplina;
import br.com.iris_api.entity.Itinerario;
import br.com.iris_api.entity.PeriodoInscricao;
import br.com.iris_api.entity.Professor;
import br.com.iris_api.entity.Turma;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.ItinerarioRepository;
import br.com.iris_api.repository.PeriodoInscricaoRepository;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.repository.TurmaRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AlunoRepository alunoRepo;
    @Autowired
    private TurmaRepository turmaRepo;
    @Autowired
    private ItinerarioRepository itinerarioRepo;
    @Autowired
    private ProfessorRepository professorRepo;
    @Autowired
    private PeriodoInscricaoRepository periodoInscricaoRepo;

    @Override
    public void run(String... args) {
        // Professores
        Professor profAna = new Professor();
        profAna.setNome("Prof. Ana");
        profAna.setCpf("11122233344");
        profAna.setSenha("senha123");
        profAna.setRole("PROFESSOR");
        profAna.setEnabled(true);

        Professor profCarlos = new Professor();
        profCarlos.setNome("Prof. Carlos");
        profCarlos.setCpf("22233344455");
        profCarlos.setSenha("senha123");
        profCarlos.setRole("PROFESSOR");
        profCarlos.setEnabled(true);

        professorRepo.save(profAna);
        professorRepo.save(profCarlos);

        // Criação de turmas e disciplinas associadas (cada disciplina em apenas uma
        // turma)
        Disciplina mat = new Disciplina();
        mat.setNome("Matemática");
        mat.setProfessor(profAna);

        Turma turma1 = new Turma();
        turma1.setIdentificador("1A");
        turma1.setSala("Sala 101");
        turma1.setTemporadaLetiva("2025.1");
        turma1.setDisciplinas(List.of(mat));
        turmaRepo.save(turma1);

        Disciplina hist = new Disciplina();
        hist.setNome("História");
        hist.setProfessor(profCarlos);

        Turma turma2 = new Turma();
        turma2.setIdentificador("2B");
        turma2.setSala("Sala 202");
        turma2.setTemporadaLetiva("2025.1");
        turma2.setDisciplinas(List.of(hist));
        turmaRepo.save(turma2);

        // Criação de alunos
        Aluno aluno1 = new Aluno();
        aluno1.setNome("João da Silva");
        aluno1.setCpf("99988877766");
        aluno1.setSenha("senha123");
        aluno1.setRole("ALUNO");
        aluno1.setEnabled(true);
        aluno1.setTurma(turma1);
        alunoRepo.save(aluno1);

        Aluno aluno2 = new Aluno();
        aluno2.setNome("Maria Souza");
        aluno2.setCpf("88877766655");
        aluno2.setSenha("senha123");
        aluno2.setRole("ALUNO");
        aluno2.setEnabled(true);
        aluno2.setTurma(turma1);
        alunoRepo.save(aluno2);

        Aluno aluno3 = new Aluno();
        aluno3.setNome("Pedro Lima");
        aluno3.setCpf("77766655544");
        aluno3.setSenha("senha123");
        aluno3.setRole("ALUNO");
        aluno3.setEnabled(true);
        aluno3.setTurma(turma2);
        alunoRepo.save(aluno3);

        Aluno aluno4 = new Aluno();
        aluno4.setNome("Ana Paula");
        aluno4.setCpf("66655544433");
        aluno4.setSenha("senha123");
        aluno4.setRole("ALUNO");
        aluno4.setEnabled(true);
        aluno4.setTurma(turma2);
        alunoRepo.save(aluno4);

        // Períodos de inscrição
        PeriodoInscricao periodo1 = new PeriodoInscricao();
        periodo1.setDataFim(LocalDate.now().plusDays(30));
        periodo1.setTurmasPermitidas(List.of(turma1));
        periodoInscricaoRepo.save(periodo1);

        PeriodoInscricao periodo2 = new PeriodoInscricao();
        periodo2.setDataFim(LocalDate.now().plusDays(45));
        periodo2.setTurmasPermitidas(List.of(turma2));
        periodoInscricaoRepo.save(periodo2);

        // Itinerários (cada disciplina apenas em um itinerário)
        Disciplina port = new Disciplina();
        port.setNome("Português");
        port.setProfessor(profAna);

        Itinerario it1 = new Itinerario();
        it1.setNome("Ciências Exatas");
        it1.setTipo("Técnico");
        it1.setQtdVagas(30);
        it1.setInscricao(periodo1);
        it1.setDisciplinas(List.of(port));
        it1.setAlunos(new ArrayList<>(List.of(aluno1, aluno2)));
        itinerarioRepo.save(it1);

        Disciplina geo = new Disciplina();
        geo.setNome("Geografia");
        geo.setProfessor(profCarlos);

        Itinerario it2 = new Itinerario();
        it2.setNome("Ciências Humanas");
        it2.setTipo("Técnico");
        it2.setQtdVagas(25);
        it2.setInscricao(periodo2);
        it2.setDisciplinas(List.of(geo));
        it2.setAlunos(new ArrayList<>(List.of(aluno3, aluno4)));
        itinerarioRepo.save(it2);

        // Associar itinerários aos alunos (mantendo mesma lógica)
        aluno1.setItinerarios(List.of(it1));
        aluno2.setItinerarios(List.of(it1));
        aluno3.setItinerarios(List.of(it2));
        aluno4.setItinerarios(List.of(it2));
        alunoRepo.save(aluno1);
        alunoRepo.save(aluno2);
        alunoRepo.save(aluno3);
        alunoRepo.save(aluno4);
    }
}