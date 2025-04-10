package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import java.util.List;

@Entity
@Schema(description = "Representa um curso realizado")
public class Curso extends PanacheEntity {

    @NotBlank(message = "O nome do curso é obrigatório")
    @Schema(description = "Nome do curso", example = "Formação Java")
    public String nome;

    @NotBlank(message = "A descrição é obrigatória")
    @Schema(description = "Descrição do curso", example = "Curso completo de Java com Spring Boot")
    public String descricao;

    @NotNull(message = "A data de início é obrigatória")
    @Schema(description = "Data de início do curso", example = "2023-01-15")
    public LocalDate dataInicio;

    @NotNull(message = "A data de término é obrigatória")
    @Schema(description = "Data de término do curso", example = "2023-05-30")
    public LocalDate dataFim;

    @ManyToOne
    @NotNull(message = "A escola é obrigatória")
    @Schema(description = "Escola onde o curso foi realizado")
    public Escola escola;

    @ManyToMany
    @Schema(description = "Linguagens ensinadas no curso")
    public List<LinguagemAprendida> linguagens;

}
