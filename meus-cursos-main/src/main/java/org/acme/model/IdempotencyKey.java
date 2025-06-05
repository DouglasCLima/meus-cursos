package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

@Entity
public class IdempotencyKey extends PanacheEntity {

    @Column(unique = true)
    public String chave;

    @Lob
    public String respostaJson;
}
