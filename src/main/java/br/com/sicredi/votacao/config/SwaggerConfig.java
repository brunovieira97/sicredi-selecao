package br.com.sicredi.votacao.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {	
	private String groupedApiName = "sicredi-votacao-api";

	private static final String
		INFO_TITLE = "Sicredi - API de Votação",
		INFO_DESCRIPTION = "API para controle de sessões de votação, parte do desafio técnico para o cargo de Dev no Sicredi",
		INFO_VERSION = "v1",
		INFO_CONTACT_NAME = "Bruno Vieira",
		INFO_CONTACT_URL = "https://github.com/brunovieira97",
		INFO_CONTACT_EMAIL = "bruno.vieira97@outlook.com";
	
	@Bean
	public GroupedOpenApi groupedOpenApi() {
		String[] packagesToScan = {"br.com.sicredi.votacao.controller"};
		return GroupedOpenApi.builder()
			.group(groupedApiName)
			.pathsToMatch("/**")
			.packagesToScan(packagesToScan)
			.build();
	}

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(this.getApiInfo());
	}

	private Info getApiInfo() {
		return new Info()
			.title(INFO_TITLE)
			.description(INFO_DESCRIPTION)
			.version(INFO_VERSION)
			.contact(
				new Contact()
					.name(INFO_CONTACT_NAME)
					.email(INFO_CONTACT_EMAIL)
					.url(INFO_CONTACT_URL)
			);
	}
}
