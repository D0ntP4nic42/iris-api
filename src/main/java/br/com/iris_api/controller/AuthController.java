package br.com.iris_api.controller;

import java.time.Instant;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.iris_api.dto.LoginDTO;
import br.com.iris_api.entity.User;
import br.com.iris_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Rotas de autenticação")
public class AuthController {
	@Autowired
	private Environment env;

	@Autowired
	private JwtEncoder jwtEncoder;

	@Autowired
	private UserService userService;

	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

	@Operation(summary = "Realizar login", description = "Recebe CPF e senha e retorna um token JWT válido caso as credenciais estejam corretas.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login bem-sucedido, token JWT retornado", content = @Content(mediaType = "application/json", schema = @Schema(type = "object", example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"))),
			@ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content()),
			@ApiResponse(responseCode = "401", description = "Credenciais inválidas", content = @Content()),
			@ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content()) })
	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
		var user = userService.findByUsername(loginDTO.cpf());

		if (PASSWORD_ENCODER.matches(loginDTO.senha(), user.getSenha()) && user.isEnabled()) {
			return ResponseEntity.ok(Collections.singletonMap("token", gerarAccessToken(user)));
		}

		throw new BadCredentialsException("Credenciais inválidas");
	}

	private String gerarAccessToken(User user) {
		var now = Instant.now();
		var expiresIn = env.getActiveProfiles()[0].equals("local") ? 86400 : 3600;

		var claims = JwtClaimsSet.builder().issuer("iris-api").subject(user.getCpf().toString()).issuedAt(now)
				.claim("role", user.getRole()).expiresAt(now.plusSeconds(expiresIn)).build();

		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

		return jwtValue;
	}
}