package br.com.iris_api.filtros;

import java.util.List;
import java.util.Map;

import br.com.iris_api.entity.Horario;

public record ItinerarioFilter (String nome, String tipo, int qtdVagas, Map<String, String> disciplinas, List<Horario> horarios, List<String> alunos) {

}
