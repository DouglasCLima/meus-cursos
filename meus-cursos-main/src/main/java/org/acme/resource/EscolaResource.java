package org.acme.resource;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.dtos.EscolaDTO;
import org.acme.model.Escola;
import org.acme.ratelimit.RateLimited;
import org.eclipse.microprofile.openapi.annotations.Operation;

import java.util.List;

@Path("/api/v1/escolas")
@Produces("application/json")
@Consumes("application/json")
public class EscolaResource {

    @GET
    @Operation(summary = "Listar escolas")
    @RateLimited(limit = 8)
    public List<Escola> listar() {
        return Escola.listAll();
    }

    @POST
    @Transactional
    @Operation(summary = "Cadastrar escola")
    @RateLimited(limit = 4)
    public Response adicionar(@Valid EscolaDTO dto) {
        Escola escola = new Escola();
        escola.nome = dto.nome;
        escola.persist();
        return Response.status(Response.Status.CREATED).entity(escola).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Excluir escola")
    @RateLimited(limit = 2)
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = Escola.deleteById(id);
        return deleted ? Response.noContent().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar escola por ID")
    @RateLimited(limit = 6)
    public Response buscarPorId(@PathParam("id") Long id) {
        Escola escola = Escola.findById(id);
        if (escola == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(escola).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualizar escola")
    @RateLimited(limit = 3)
    public Response atualizar(@PathParam("id") Long id, @Valid EscolaDTO dto) {
        Escola escola = Escola.findById(id);
        if (escola == null) return Response.status(Response.Status.NOT_FOUND).build();
        escola.nome = dto.nome;
        return Response.ok(escola).build();
    }
}
