package br.com.iris_api.controller;

import java.time.Instant;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.iris_api.dto.LoginDTO;
import br.com.iris_api.entity.User;
import br.com.iris_api.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private JwtEncoder jwtEncoder;
	
	@Autowired
	private UserService userService;
	
	public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
	
	
//	public AuthController(JwtEncoder jwtEncoder, UserService userService) {
//		this.jwtEncoder = jwtEncoder;
//	}
	
	@PostMapping("/login")
	public ResponseEntity login(LoginDTO loginDTO) {
		var user = userService.findByUsername(loginDTO.cpf());
		
		if (user.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("mensagem", "Usuário não encontrado"));
		}
		
	    if (PASSWORD_ENCODER.matches(loginDTO.senha(), user.get().getSenha())) {
	    	return ResponseEntity.ok(Collections.singletonMap("token", gerarAccessToken(user.get())));
	    }
	    
	    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("mensagem", "Senha ou usuário inválidos"));
    }
	
	private String gerarAccessToken(User user) {
		var now = Instant.now();
		var expiresIn = 300L;
		
		var claims = JwtClaimsSet.builder()
				.issuer("iris-api")
				.subject(user.getId().toString())
				.issuedAt(now)
				.claim("role", user.getRole())
				.expiresAt(now.plusSeconds(expiresIn))
				.build();
				
		var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
		
		return jwtValue;
	}
}