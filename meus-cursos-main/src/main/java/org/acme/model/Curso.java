package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Curso extends PanacheEntity {

    @NotBlank
    public String nome;

    @NotNull
    public Integer cargaHoraria;

    @ManyToOne
    @JoinColumn(name = "escola_id")
    public Escola escola;

    @ManyToMany
    @JoinTable(name = "curso_linguagem",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "linguagem_id"))
    public List<LinguagemAprendida> linguagens;
}
