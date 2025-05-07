package br.com.iris_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}
	
	@ExceptionHandler(EntityExistsException.class)
	public ResponseEntity<String> handleEntityExists(EntityExistsException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(e.getMessage());
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentials(BadCredentialsException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(e.getMessage());
	}
	
	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGenericException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Um erro desconhecido ocorreu");
    }
}
