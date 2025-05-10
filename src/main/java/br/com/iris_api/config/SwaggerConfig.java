package br.com.iris_api.config;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.media.Schema;

@Configuration
public class SwaggerConfig {

	@Value("${user.cpf.admin}")
	private String defaultLogin;

	@Value("${user.password.admin}")
	private String defaultPassword;

	@Autowired
	Environment env;

	@Bean
	public OpenApiCustomizer customiseLoginExample() {
		if (env.getActiveProfiles()[0].equals("local")) {
			return openApi -> {
				var schema = openApi.getComponents().getSchemas().get("LoginDTO");
				if (schema != null && schema.getProperties() != null) {
					Schema<?> cpfSchema = (Schema<?>) schema.getProperties().get("cpf");
					Schema<?> senhaSchema = (Schema<?>) schema.getProperties().get("senha");

					if (cpfSchema != null) {
						cpfSchema.setExample(defaultLogin);
					}
					if (senhaSchema != null) {
						senhaSchema.setExample(defaultPassword);
					}
				}
			};
		}
		
		return openApi -> {};
	}
}
