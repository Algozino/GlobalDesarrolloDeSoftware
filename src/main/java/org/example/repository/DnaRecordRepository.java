package org.example.repository;

import org.example.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    // Usado por MutantService para deduplicación (caché de BD)
    Optional<DnaRecord> findByDnaHash(String dnaHash);

    // Usado por StatsService para las estadísticas
    long countByIsMutant(boolean isMutant);
}
