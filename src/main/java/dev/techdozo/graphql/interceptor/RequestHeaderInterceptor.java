package dev.techdozo.graphql.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Component
public class RequestHeaderInterceptor implements WebGraphQlInterceptor {

  @Override
  public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {

    var roleHeader = request.getHeaders().get("role");

    if (roleHeader != null) {
      request.configureExecutionInput(
          (executionInput, builder) ->
              builder.graphQLContext(Collections.singletonMap("role", roleHeader.get(0))).build());
    }

    return chain.next(request);
  }
}
