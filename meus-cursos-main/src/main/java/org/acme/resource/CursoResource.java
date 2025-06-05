package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.acme.dtos.CursoDTO;
import org.acme.model.Curso;
import org.acme.model.IdempotencyKey;

@Path("/api/v1/cursos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CursoResource {

    @POST
    @Transactional
    @Operation(summary = "Cadastrar um novo curso com suporte a idempotência")
    @APIResponse(responseCode = "201", description = "Curso criado com sucesso")
    public Response adicionarComIdempotencia(@HeaderParam("Idempotency-Key") String idempotencyKey,
                                             @Valid CursoDTO dto) {

        if (idempotencyKey != null) {
            var existente = IdempotencyKey.find("chave", idempotencyKey).firstResult();
            if (existente != null) {
                return Response.ok(existente.respostaJson, MediaType.APPLICATION_JSON).build();
            }
        }

        Curso curso = new Curso();
        curso.nome = dto.nome;
        curso.cargaHoraria = dto.cargaHoraria;
        curso.persist();

        Jsonb jsonb = JsonbBuilder.create();
        String respostaJson = jsonb.toJson(curso);

        if (idempotencyKey != null) {
            IdempotencyKey novaChave = new IdempotencyKey();
            novaChave.chave = idempotencyKey;
            novaChave.respostaJson = respostaJson;
            novaChave.persist();
        }

        return Response.status(Response.Status.CREATED).entity(curso).build();
    }

    // Aqui você pode adicionar outros endpoints como listar, buscar por ID, atualizar, deletar etc.
}
