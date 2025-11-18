package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "dna_records", indexes = {
        // indice único para la deduplicación por hash (OBLIGATORIO)
        @Index(name = "idx_dna_hash", columnList = "dnaHash", unique = true),
        // indice para acelerar los conteos de /stats
        @Index(name = "idx_is_mutant", columnList = "isMutant")
})

@Getter
@Setter
@NoArgsConstructor

public class DnaRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64) // SHA-256 tiene 64 chars
    private String dnaHash;

    @Column(nullable = false)
    private boolean isMutant;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
