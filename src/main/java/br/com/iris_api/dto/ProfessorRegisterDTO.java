package br.com.iris_api.dto;

public record ProfessorRegisterDTO(boolean isCoordenador, String nome, String cpf, String email, String senha) {

}
