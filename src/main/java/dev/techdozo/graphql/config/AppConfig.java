package dev.techdozo.graphql.config;

import dev.techdozo.graphql.directive.AuthDirective;
import graphql.validation.rules.OnValidationErrorStrategy;
import graphql.validation.rules.ValidationRules;
import graphql.validation.schemawiring.ValidationSchemaWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class AppConfig {
  @Bean
  public RuntimeWiringConfigurer runtimeWiringConfigurer() {
    var validationRules =
        ValidationRules.newValidationRules()
            .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
            .build();
    var schemaWiring = new ValidationSchemaWiring(validationRules);

    return builder -> {
      builder.directiveWiring(schemaWiring);
      builder.directiveWiring(new AuthDirective());
    };
  }
}
