package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Entity
@Schema(description = "Representa uma linguagem aprendida em um curso")
public class LinguagemAprendida extends PanacheEntity {

    @NotBlank(message = "O nome da linguagem é obrigatório")
    @Column(unique = true)
    @Schema(description = "Nome da linguagem", example = "Java")
    public String nome;

    @ManyToMany(mappedBy = "linguagens")
    @Schema(description = "Lista de cursos em que essa linguagem foi ensinada")
    public List<Curso> cursos;
}

