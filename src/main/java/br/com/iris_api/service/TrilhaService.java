package br.com.iris_api.service;

import br.com.iris_api.dto.TrilhaDTO;
import br.com.iris_api.entity.Trilha;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.repository.TrilhaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrilhaService {

    @Autowired
    private TrilhaRepository trilhaRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;


    public void cadastrarTrilha(TrilhaDTO trilhaDTO) {
        var professor = professorRepository.findByCpf(trilhaDTO.cpfProfessor())
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado"));

        var trilha = new Trilha(trilhaDTO.nome(), trilhaDTO.identificador(), professor);

        trilhaRepository.save(trilha);
    }



}
