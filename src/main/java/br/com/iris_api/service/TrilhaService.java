package br.com.iris_api.service;

import br.com.iris_api.dto.TrilhaDTO;
import br.com.iris_api.entity.Trilha;
import br.com.iris_api.repository.AlunoRepository;
import br.com.iris_api.repository.ProfessorRepository;
import br.com.iris_api.repository.TrilhaRepository;
import jakarta.persistence.EntityExistsException;
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

        var trilha = new Trilha(trilhaDTO.nome(), trilhaDTO.identificador(), professor, trilhaDTO.vagas());

        trilhaRepository.save(trilha);
    }

    public void cadastrarAluno(String matricula, String identificador){
        var aluno = alunoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado"));

        var trilha = trilhaRepository.findByIdentificador(identificador)
                .orElseThrow(() -> new EntityNotFoundException("Trilha não encontrada"));

        if (trilha.getAlunos().contains(aluno)) {
            throw new EntityExistsException("Aluno já cadastrado na trilha " + trilha.getNome());
        }
        if(aluno.getTrilha() != null){
            throw new EntityExistsException("Aluno já cadastrado em outra trilha");
        }

        aluno.setTrilha(trilha);
        trilha.addAluno(aluno);
        alunoRepository.save(aluno);
        trilhaRepository.save(trilha);
    }

    public Trilha findByIdentificador(String identificador) {
        return trilhaRepository.findByIdentificador(identificador)
                .orElseThrow(() -> new EntityNotFoundException("Trilha não encontrada"));
    }



}
