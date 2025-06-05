package org.acme.resource;

import org.acme.dtos.LinguagemAprendidaDTO;
import org.acme.model.IdempotencyKey;
import org.acme.model.LinguagemAprendida;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Map;

@Path("/api/v1/linguagens")
@Tag(name = "Linguagens", description = "Gerencia linguagens aprendidas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LinguagemAprendidaResource {

    @GET
    @Operation(summary = "Listar todas as linguagens")
    public List<LinguagemAprendida> listar() {
        return LinguagemAprendida.listAll();
    }

    @POST
    @Path("/api/v1/linguagens")
    @Transactional
    @Operation(summary = "Cadastrar nova linguagem com idempotência")
    @APIResponse(responseCode = "201", description = "Linguagem criada com sucesso")
    public Response adicionar(@HeaderParam("Idempotency-Key") String idempotencyKey, @Valid LinguagemAprendidaDTO dto) {
        if (idempotencyKey != null) {
            var existente = IdempotencyKey.find("chave", idempotencyKey).firstResult();
            if (existente != null) {
                return Response.ok(existente.respostaJson).build();
            }
        }

        LinguagemAprendida linguagem = new LinguagemAprendida();
        linguagem.nome = dto.nome;
        linguagem.nivel = dto.nivel;
        linguagem.persist();

        String resposta = "{\"id\":" + linguagem.id + ",\"nome\":\"" + linguagem.nome + "\"}";

        if (idempotencyKey != null) {
            IdempotencyKey chave = new IdempotencyKey();
            chave.chave = idempotencyKey;
            chave.respostaJson = resposta;
            chave.persist();
        }

        return Response.status(Response.Status.CREATED).entity(linguagem).build();
    }


    @PUT
    @Path("/{id}")
    @Transactional
    @Operation(summary = "Atualizar linguagem")
    public Response atualizar(@PathParam("id") Long id, @Valid LinguagemAprendidaDTO dto) {
        return LinguagemAprendida.<LinguagemAprendida>findByIdOptional(id)
                .map(linguagem -> {
                    linguagem.nome = dto.nome;
                    linguagem.nivel = dto.nivel;
                    return Response.ok(linguagem).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("erro", "Linguagem não encontrada")).build());
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
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(Map.of("erro", "Linguagem não encontrada")).build());
    }

    @GET
    @Path("/por-nome/{nome}")
    @Operation(summary = "Buscar linguagem por nome")
    public List<LinguagemAprendida> buscarPorNome(@PathParam("nome") String nome) {
        return LinguagemAprendida.list("LOWER(nome) LIKE LOWER(?1)", "%" + nome + "%");
    }
}
