package br.com.iris_api.dto;

import java.util.List;

public record ItinerarioDTO(String nome, String tipo, int qtdVagas, List<HorarioDTO> horarios) {

}
