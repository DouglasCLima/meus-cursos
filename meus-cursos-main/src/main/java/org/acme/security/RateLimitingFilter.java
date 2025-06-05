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
@Priority(Priorities.USER)  // Executa após autenticação
public class RateLimitingFilter implements ContainerRequestFilter {

    private static final int MAX_REQUESTS = 5;  // max requisições permitidas
    private static final long WINDOW_SIZE_MS = 60 * 1000; // 1 minuto

    // Armazena para cada cliente: [primeiro timestamp da janela, contador de requisições]
    private Map<String, RequestCounter> requests = new ConcurrentHashMap<>();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String clientIp = getClientIp(requestContext);
        long now = Instant.now().toEpochMilli();

        RequestCounter counter = requests.get(clientIp);

        if (counter == null || now - counter.windowStart > WINDOW_SIZE_MS) {
            // Nova janela
            counter = new RequestCounter(now, 1);
            requests.put(clientIp, counter);
        } else {
            // Janela atual ainda válida
            if (counter.count >= MAX_REQUESTS) {
                abortWithTooManyRequests(requestContext, MAX_REQUESTS);
                return;
            }
            counter.count++;
        }

        // Adicionar cabeçalhos informativos
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

    private String getClientIp(ContainerRequestContext requestContext) {
        // Simples: tenta pegar do header X-Forwarded-For ou usa IP remoto
        String ip = requestContext.getHeaderString("X-Forwarded-For");
        if (ip == null) {
            ip = requestContext.getUriInfo().getRequestUri().getHost();
        }
        return ip;
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

