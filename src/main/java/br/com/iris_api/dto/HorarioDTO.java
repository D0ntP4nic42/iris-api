package br.com.iris_api.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record HorarioDTO(DayOfWeek diaDaSemana, LocalTime comeco, LocalTime fim) {

}
