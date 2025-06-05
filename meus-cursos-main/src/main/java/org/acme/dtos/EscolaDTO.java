package org.acme.dtos;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "EscolaDTO", description = "Dados para criação/atualização de escola")
public class EscolaDTO {

    @NotBlank(message = "O nome da escola é obrigatório")
    @Schema(description = "Nome da escola", example = "Alura", required = true)
    public String nome;

    @NotBlank(message = "O país é obrigatório")
    @Schema(description = "País de origem da escola", example = "Brasil", required = true)
    public String pais;
}

