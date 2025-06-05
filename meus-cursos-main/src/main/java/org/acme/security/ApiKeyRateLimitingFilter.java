package org.acme.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Provider
@Priority(Priorities.USER)  // Executa depois da autenticação
public class ApiKeyRateLimitingFilter implements ContainerRequestFilter {

    private static final int MAX_REQUESTS = 10;  // Limite por janela (ex: 10 reqs)
    private static final long WINDOW_SIZE_MS = 60 * 1000; // Janela de 1 minuto

    // Map para contar requisições por chave API
    private Map<String, RequestCounter> requests = new ConcurrentHashMap<>();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String apiKey = requestContext.getHeaderString("X-API-Key");
        if (apiKey == null || apiKey.isEmpty()) {
            // Sem chave API, bloqueia acesso (ou pode liberar, dependendo da regra)
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"error\":\"Chave API obrigatória\"}")
                            .build());
            return;
        }

        long now = Instant.now().toEpochMilli();

        RequestCounter counter = requests.get(apiKey);

        if (counter == null || now - counter.windowStart > WINDOW_SIZE_MS) {
            // Nova janela
            counter = new RequestCounter(now, 1);
            requests.put(apiKey, counter);
        } else {
            // Janela atual válida
            if (counter.count >= MAX_REQUESTS) {
                abortWithTooManyRequests(requestContext, MAX_REQUESTS);
                return;
            }
            counter.count++;
        }

        // Cabeçalhos informativos
        requestContext.getHeaders().add("X-RateLimit-Limit", String.valueOf(MAX_REQUESTS));
        requestContext.getHeaders().add("X-RateLimit-Remaining", String.valueOf(MAX_REQUESTS - counter.count));
    }

    private void abortWithTooManyRequests(ContainerRequestContext requestContext, int limit) {
        requestContext.abortWith(
                Response.status(429)
                        .entity("{\"error\": \"Too Many Requests. Max allowed: " + limit + "\"}")
                        .build()
        );
    }

    private static class RequestCounter {
        long windowStart;
        int count;

        RequestCounter(long windowStart, int count) {
            this.windowStart = windowStart;
            this.count = count;
        }
    }
}
