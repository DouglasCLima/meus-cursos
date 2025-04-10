package org.acme.resource;

import org.acme.model.Curso;
import org.acme.model.Escola;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import java.net.URI;
import java.util.List;

@Path("/cursos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Cursos", description = "Operações relacionadas aos cursos")
public class CursoResource {

    @GET
    @Operation(summary = "Listar todos os cursos")
    public List<Curso> listar() {
        return Curso.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar curso por ID")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Curso.findByIdOptional(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Transactional
    @Operation(summary = "Cadastrar novo curso")
    public Response cadastrar(@Valid Curso curso) {
        if (Escola.findById(curso.escola.id) == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Escola informada não encontrada").build();
        }

        curso.persist();
        return Response.created(URI.create("/cursos/" + curso.id)).entity(curso).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualizar curso")
    public Response atualizar(@PathParam("id") Long id, @Valid Curso cursoAtualizado) {
        return Curso.<Curso>findByIdOptional(id)
                .map(curso -> {
                    curso.nome = cursoAtualizado.nome;
                    curso.descricao = cursoAtualizado.descricao;
                    curso.dataInicio = cursoAtualizado.dataInicio;
                    curso.dataFim = cursoAtualizado.dataFim;
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
