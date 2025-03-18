package br.com.iris_api.service;


import br.com.iris_api.entity.Turma;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.repository.TurmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurmaService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    public List<Turma> listarTurmas(){
        return turmaRepository.findAll();
    }

    public Optional<Turma> listaTurmaPorIdentificador(String identificador){
        return turmaRepository.findByIdentificador(identificador);
    }
}
