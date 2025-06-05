package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
public class LinguagemAprendida extends PanacheEntity {

    @NotBlank
    public String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    public NivelConhecimento nivel;

    @ManyToMany(mappedBy = "linguagens")
    public List<Curso> cursos;
}
