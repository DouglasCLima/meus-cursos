package org.acme.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Provider
@Priority(Priorities.AUTHENTICATION) // Executa antes de filtros de autorização
public class ApiKeyAuthFilter implements ContainerRequestFilter {

    // Simulação: conjunto fixo de chaves válidas (ideal: armazenar em banco/config)
    private static final Set<String> VALID_API_KEYS = new HashSet<>();

    static {
        VALID_API_KEYS.add("chave123");
        VALID_API_KEYS.add("chave456");
        // Adicione outras chaves válidas aqui
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String apiKey = requestContext.getHeaderString("X-API-Key");

        if (apiKey == null || apiKey.isBlank() || !VALID_API_KEYS.contains(apiKey)) {
            // Chave ausente ou inválida - bloqueia acesso
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Chave API inválida ou ausente\"}")
                    .build());
        }
        // Caso a chave seja válida, segue normalmente
    }
}


