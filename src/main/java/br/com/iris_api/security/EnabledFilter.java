package br.com.iris_api.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.iris_api.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EnabledFilter extends OncePerRequestFilter {
	@Autowired
	private UserService userService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		
		var user = userService.findByUsername(authentication.getName());
		
		if (!user.isEnabled()) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        response.setContentType("application/json");
	        response.getWriter().write("{\"error\": \"Usuário desabilitado.\"}");
	        return;
		}
		
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    String path = request.getRequestURI();

	    return path.startsWith("/auth") ||
	           path.startsWith("/swagger") ||
	           path.startsWith("/v3");
	}
}
