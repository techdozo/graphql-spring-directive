package dev.techdozo.graphql.directive;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthDirective implements SchemaDirectiveWiring {

  @Override
  public GraphQLFieldDefinition onField(
      SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {

    // Skipping fields for which directive auth is not applied as this is called for every field.
    if (environment.getAppliedDirective("auth") == null) {
      return environment.getElement();
    }

    var schemaDirectiveRole = environment.getAppliedDirective("auth").getArgument("role").getValue();

    var field = environment.getElement();
    var parentType = environment.getFieldsContainer();
    //
    // build a data fetcher that first checks authorisation roles before then calling the original
    // data fetcher
    //
    var originalDataFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);

    // at runtime authDataFetcher is called
    DataFetcher<?> authDataFetcher =
        dataFetchingEnvironment -> {
          var graphQlContext = dataFetchingEnvironment.getGraphQlContext();
            // role is set in context in interceptor code
          String userRole = graphQlContext.get("role");

          if (userRole != null && userRole.equals(schemaDirectiveRole)) {
            return originalDataFetcher.get(dataFetchingEnvironment);
          } else {
            return null;
          }
        };
    //
    // now change the field definition to have the new auth data fetcher
    environment.getCodeRegistry().dataFetcher(parentType, field, authDataFetcher);
    return field;
  }
}
