package org.acme.resource;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.CursoDTO;
import org.acme.model.Curso;
import org.acme.ratelimit.RateLimited;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;

@Path("/api/v1/cursos")
@Produces("application/json")
@Consumes("application/json")
public class CursoResource {

    @GET
    @Operation(summary = "Listar todos os cursos")
    @RateLimited(limit = 10, intervalSeconds = 60)
    public List<Curso> listar() {
        return Curso.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar curso por ID")
    @APIResponse(responseCode = "200", description = "Curso encontrado")
    @APIResponse(responseCode = "404", description = "Curso não encontrado")
    @RateLimited(limit = 10)
    public Response buscarPorId(@PathParam("id") Long id) {
        Curso curso = Curso.findById(id);
        if (curso == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(curso).build();
    }

    @POST
    @Transactional
    @Operation(summary = "Cadastrar um novo curso")
    @APIResponse(responseCode = "201", description = "Curso criado com sucesso")
    @RateLimited(limit = 5)
    public Response adicionar(@Valid CursoDTO dto) {
        Curso curso = new Curso();
        curso.nome = dto.nome;
        curso.persist();
        return Response.status(Response.Status.CREATED).entity(curso).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualizar um curso")
    @RateLimited(limit = 5)
    public Response atualizar(@PathParam("id") Long id, @Valid CursoDTO dto) {
        Curso curso = Curso.findById(id);
        if (curso == null) return Response.status(Response.Status.NOT_FOUND).build();

        curso.nome = dto.nome;
        return Response.ok(curso).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Deletar um curso")
    @RateLimited(limit = 3)
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = Curso.deleteById(id);
        if (deleted) return Response.noContent().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}