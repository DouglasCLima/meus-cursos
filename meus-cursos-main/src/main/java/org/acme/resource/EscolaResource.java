package org.acme.resource;

import io.quarkus.panache.common.Sort;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.CursoDTO;
import org.acme.model.Curso;
import org.acme.model.IdempotencyKey;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/v1/cursos")
@Tag(name = "Cursos", description = "Gerencia os cursos da aplicação")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CursoResource {

    @GET
    @Operation(summary = "Listar todos os cursos")
    public List<Curso> listar() {
        return Curso.listAll(Sort.by("nome"));
    }

    @POST
    @Transactional
    @Operation(summary = "Cadastrar um novo curso com suporte a idempotência")
    @APIResponse(responseCode = "201", description = "Curso criado com sucesso")
    public Response adicionar(@HeaderParam("Idempotency-Key") String idempotencyKey,
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

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualizar curso")
    public Response atualizar(@PathParam("id") Long id,
                              @Valid Curso cursoAtualizado) {
        return Curso.<Curso>findByIdOptional(id)
                .map(curso -> {
                    curso.nome = cursoAtualizado.nome;
                    curso.cargaHoraria = cursoAtualizado.cargaHoraria;
                    curso.escola = cursoAtualizado.escola;
                    return Response.ok(curso).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Remover curso")
    public Response deletar(@PathParam("id") Long id) {
        return Curso.findByIdOptional(id)
                .map(curso -> {
                    curso.delete();
                    return Response.noContent().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/por-escola/{escolaId}")
    @Operation(summary = "Listar cursos por escola")
    public List<Curso> listarPorEscola(@PathParam("escolaId") Long escolaId) {
        return Curso.list("escola.id", escolaId);
    }
}
