package br.com.iris_api.dto;

import java.time.LocalDate;
import java.util.List;

public record PeriodoInscricaoDTO (LocalDate dataFim, List<String> itinerariosNome, List<String> turmasPermitidasNome) {
}
