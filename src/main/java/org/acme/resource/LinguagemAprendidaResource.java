package org.acme.resource;


import org.acme.model.LinguagemAprendida;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.ws.rs.PathParam;


import java.net.URI;
import java.util.List;

@Path("/linguagens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Linguagens Aprendidas", description = "Operações relacionadas às linguagens aprendidas")
public class LinguagemAprendidaResource {

    @GET
    @Operation(summary = "Listar todas as linguagens")
    public List<LinguagemAprendida> listar() {
        return LinguagemAprendida.listAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar linguagem por ID")
    public Response buscarPorId(@PathParam("id") Long id) {
        return LinguagemAprendida.findByIdOptional(id)
                .map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @POST
    @Transactional
    @Operation(summary = "Cadastrar nova linguagem")
    public Response cadastrar(@Valid LinguagemAprendida linguagem) {
        linguagem.persist();
        return Response.created(URI.create("/linguagens/" + linguagem.id)).entity(linguagem).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualizar linguagem")
    public Response atualizar(@PathParam("id") Long id, @Valid LinguagemAprendida atualizada) {
        return LinguagemAprendida.<LinguagemAprendida>findByIdOptional(id)
                .map(linguagem -> {
                    linguagem.nome = atualizada.nome;
                    return Response.ok(linguagem).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Remover linguagem")
    public Response deletar(@PathParam("id") Long id) {
        return LinguagemAprendida.findByIdOptional(id)
                .map(linguagem -> {
                    linguagem.delete();
                    return Response.noContent().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/por-nome/{nome}")
    @Operation(summary = "Buscar linguagem por nome")
    public List<LinguagemAprendida> buscarPorNome(@PathParam("nome") String nome) {
        return LinguagemAprendida.list("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%");
    }
}

