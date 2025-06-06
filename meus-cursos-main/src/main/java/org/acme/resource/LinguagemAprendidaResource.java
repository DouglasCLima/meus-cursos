package org.acme.resource;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.LinguagemAprendidaDTO;
import org.acme.model.LinguagemAprendida;
import org.acme.ratelimit.RateLimited;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.List;

@Path("/api/v1/linguagens")
@Produces("application/json")
@Consumes("application/json")
public class LinguagemAprendidaResource {

    @GET
    @Operation(summary = "Listar linguagens")
    @RateLimited(limit = 6)
    public List<LinguagemAprendida> listar() {
        return LinguagemAprendida.listAll();
    }

    @POST
    @Transactional
    @Operation(summary = "Cadastrar nova linguagem")
    @RateLimited(limit = 3)
    public Response adicionar(@Valid LinguagemAprendidaDTO dto) {
        LinguagemAprendida linguagem = new LinguagemAprendida();
        linguagem.nome = dto.nome;
        linguagem.nivel = dto.nivel;
        linguagem.persist();
        return Response.status(Response.Status.CREATED).entity(linguagem).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @RateLimited(limit = 2)
    public Response excluir(@PathParam("id") Long id) {
        boolean deleted = LinguagemAprendida.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar linguagem por ID")
    @RateLimited(limit = 5)
    public Response buscarPorId(@PathParam("id") Long id) {
        LinguagemAprendida linguagem = LinguagemAprendida.findById(id);
        if (linguagem == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(linguagem).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualizar linguagem")
    @RateLimited(limit = 3)
    public Response atualizar(@PathParam("id") Long id, @Valid LinguagemAprendidaDTO dto) {
        LinguagemAprendida linguagem = LinguagemAprendida.findById(id);
        if (linguagem == null) return Response.status(Response.Status.NOT_FOUND).build();
        linguagem.nome = dto.nome;
        linguagem.nivel = dto.nivel;
        return Response.ok(linguagem).build();
    }

}
