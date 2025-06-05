package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;

@Entity
public class ApiKey extends PanacheEntity {

    @Column(unique = true, nullable = false)
    public String chave;

    @Column(nullable = false)
    public String usuario;  // opcional, para quem gerou a chave

    public String nivelAcesso; // opcional, para diferentes níveis (ex: read, write, admin)
}

