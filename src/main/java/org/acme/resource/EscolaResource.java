package org.acme.resource;

import org.acme.model.Escola;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Path("/escolas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Escolas", description = "Operações relacionadas às escolas")
public class EscolaResource {

    @GET
    @Operation(summary = "Listar todas as escolas", description = "Retorna uma lista de todas as escolas cadastradas")
    public List<Escola> listar() {
        return Escola.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar escola por ID", description = "Retorna uma escola específica pelo ID")
    public Response buscarPorId(@PathParam("id") Long id) {
        return Escola.findByIdOptional(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Transactional
    @Operation(summary = "Cadastrar nova escola", description = "Adiciona uma nova escola")
    public Response cadastrar(@Valid Escola escola) {
        escola.persist();
        return Response.created(URI.create("/escolas/" + escola.id)).entity(escola).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualizar escola", description = "Atualiza os dados de uma escola existente")
    public Response atualizar(@PathParam("id") Long id, @Valid Escola escolaAtualizada) {
        return Escola.<Escola>findByIdOptional(id)
                .map(escola -> {
                    escola.nome = escolaAtualizada.nome;
                    escola.localizacao = escolaAtualizada.localizacao;
                    escola.site = escolaAtualizada.site;
                    return Response.ok(escola).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Remover escola", description = "Remove uma escola do sistema")
    public Response deletar(@PathParam("id") Long id) {
        return Escola.findByIdOptional(id)
                .map(escola -> {
                    escola.delete();
                    return Response.noContent().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/buscar/{nome}")
    @Operation(summary = "Buscar escola por nome", description = "Busca escolas que contenham o texto informado no nome")
    public List<Escola> buscarPorNome(@PathParam("nome") String nome) {
        return Escola.list("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%");
    }
}
