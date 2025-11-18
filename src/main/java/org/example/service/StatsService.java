package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository dnaRecordRepository;

    @Transactional(readOnly = true)
    public StatsResponse getStats() {
        long mutantCount = dnaRecordRepository.countByIsMutant(true);
        long humanCount = dnaRecordRepository.countByIsMutant(false);
        double ratio = 0.0;

        // Manejar division por cero (como se indica en documentacion)
        if (humanCount > 0) {
            ratio = (double) mutantCount / humanCount;
        } else if (mutantCount > 0) {
            // Si hay mutantes pero no humanos, el ratio es N
            ratio = mutantCount;
        }

        // Redondear a 2 decimales para una respuesta limpia
        ratio = Math.round(ratio * 100.0) / 100.0;

        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}
