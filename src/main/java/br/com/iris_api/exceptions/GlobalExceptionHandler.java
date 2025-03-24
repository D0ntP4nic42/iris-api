package br.com.iris_api.exceptions;

import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final String RESPONSE_FIELD_NOME = "mensagem";
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity handleEntityNotFound(EntityNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap(RESPONSE_FIELD_NOME, e.getMessage()));
	}
	
	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity handleEntityExists(EntityExistsException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(Collections.singletonMap(RESPONSE_FIELD_NOME, e.getMessage()));
	}
	
	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity handleGenericException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap(RESPONSE_FIELD_NOME, "Um erro ocorreu: " + e.getMessage()));
    }
}
