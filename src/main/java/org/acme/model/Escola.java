package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Entity
@Schema(description = "Representa uma escola que oferece cursos")
public class Escola extends PanacheEntity {

    @NotBlank(message = "O nome da escola é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    @Schema(description = "Nome da escola", example = "Alura")
    public String nome;

    @NotBlank(message = "A localização é obrigatória")
    @Schema(description = "Localização da escola", example = "São Paulo - SP")
    public String localizacao;

    @Schema(description = "Site da escola", example = "https://www.alura.com.br")
    public String site;
}


