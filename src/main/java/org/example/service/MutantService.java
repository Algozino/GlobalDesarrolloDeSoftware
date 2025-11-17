package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.DnaRecord;
import org.example.exception.DnaHashCalculationException;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    @Transactional
    public boolean analyzeDna(String[] dna) {
        // calcular HASH (SHA-256)
        String hash = calculateDnaHash(dna);

        //verificar si ya existe en BD (Deduplicación)
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(hash);
        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant(); // retorna resultado cacheado
        }

        // si no existe, analizar ADN
        boolean isMutant = mutantDetector.isMutant(dna);

        // Guardar el nuevo resultado en BD
        DnaRecord newRecord = new DnaRecord();
        newRecord.setDnaHash(hash);
        newRecord.setMutant(isMutant);
        dnaRecordRepository.save(newRecord);

        return isMutant;
    }

    private String calculateDnaHash(String[] dna) {
        try {
            // unir todos los strings en uno solo para hashear
            String dnaString = String.join("", dna);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));

            // convertir bytes[] a String hexadecimal
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error al calcular el hash SHA-256", e);
        }
    }
}
