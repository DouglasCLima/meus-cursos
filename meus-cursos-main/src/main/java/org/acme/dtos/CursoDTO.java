package org.acme.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "CursoDTO", description = "Dados para criação/atualização de curso")
public class CursoDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Schema(description = "Nome do curso", example = "Curso de Quarkus", required = true)
    public String nome;

    @NotNull(message = "Carga horária é obrigatória")
    @Min(value = 1, message = "Carga horária mínima é 1 hora")
    @Schema(description = "Carga horária do curso em horas", example = "40", required = true)
    public Integer cargaHoraria;
}
