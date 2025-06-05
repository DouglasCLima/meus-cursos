package org.acme.resource;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.ApiKey;

import java.util.List;
import java.util.UUID;

@Path("/apikeys")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApiKeyResource {

    @GET
    public List<ApiKey> listar() {
        return ApiKey.listAll();
    }

    @POST
    @Transactional
    public Response criar(@QueryParam("usuario") String usuario) {
        ApiKey chave = new ApiKey();
        chave.chave = UUID.randomUUID().toString();
        chave.usuario = usuario != null ? usuario : "desconhecido";
        chave.persist();
        return Response.status(Response.Status.CREATED).entity(chave).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        ApiKey chave = ApiKey.findById(id);
        if (chave == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        chave.delete();
        return Response.noContent().build();
    }
}

