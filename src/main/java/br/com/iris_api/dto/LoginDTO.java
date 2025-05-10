package br.com.iris_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginDTO(@Schema(description = "Nome de usuário", example = "123.456.789-10") String cpf,
		@Schema(description = "Senha do usuário", example = "12345678") String senha) {

}
