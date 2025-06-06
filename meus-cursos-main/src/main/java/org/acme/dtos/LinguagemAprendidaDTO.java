package org.acme.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.acme.model.NivelConhecimento; // <-- importar o enum correto
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "LinguagemAprendidaDTO", description = "Dados para criar ou atualizar uma linguagem")
public class LinguagemAprendidaDTO {

    @NotBlank(message = "O nome da linguagem é obrigatório")
    @Schema(description = "Nome da linguagem", example = "Java", required = true)
    public String nome;

    @NotNull(message = "O nível é obrigatório")
    @Schema(description = "Nível de conhecimento", example = "INTERMEDIARIO", required = true)
    public NivelConhecimento nivel;
}
