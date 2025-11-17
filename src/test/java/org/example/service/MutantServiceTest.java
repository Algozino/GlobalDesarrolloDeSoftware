package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    @Test
    @DisplayName("Debe analizar y GUARDAR si el ADN no está en cache")
    void testAnalyzeDnaAndSave() {
        // Arrange
        String[] dna = {"AAAA", "CCCT", "GGGT", "TTTT"};
        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.empty()); // No está en cache
        when(mutantDetector.isMutant(dna)).thenReturn(true); // Es mutante

        // Act
        boolean result = mutantService.analyzeDna(dna);

        // Assert
        assertTrue(result);
        // Verificar que SÍ se llamó al detector
        verify(mutantDetector, times(1)).isMutant(dna);
        // Verificar que SÍ se guardó en BD
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe retornar resultado de CACHÉ y NO guardar ni analizar")
    void testReturnCachedResult() {
        // Arrange
        String[] dna = {"AAAA", "CCCT", "GGGT", "TTTT"};
        DnaRecord cachedRecord = new DnaRecord();
        cachedRecord.setMutant(true);

        when(dnaRecordRepository.findByDnaHash(anyString())).thenReturn(Optional.of(cachedRecord)); // SÍ está en cache

        // Act
        boolean result = mutantService.analyzeDna(dna);

        // Assert
        assertTrue(result);
        // Verificar que NUNCA se llamo al detector
        verify(mutantDetector, never()).isMutant(any());
        // Verificar que NUNCA se guardo en BD
        verify(dnaRecordRepository, never()).save(any(DnaRecord.class));
    }
}
