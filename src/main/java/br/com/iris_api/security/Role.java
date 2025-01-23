package br.com.iris_api.security;

public enum Role {
	COORDENADOR("Coordenador, professor com acesso total. Usuário único"),
    PROFESSOR("Professor, com acesso restrito"),
    ALUNO("Aluno com acesso básico");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
