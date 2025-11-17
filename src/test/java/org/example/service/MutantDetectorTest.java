package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    @Test
    @DisplayName("Debe detectar mutante con 2 secuencias")
    void testMutantWithHorizontalAndDiagonal() {
        String[] dnaClaro = {
                "AAAA", // Horizontal A
                "CACT",
                "GGAT",
                "TTTA"  // Diagonal A en A(0,0), A(1,1), A(2,2), A(3,3).
        };
        assertTrue(mutantDetector.isMutant(dnaClaro));
    }

    @Test
    @DisplayName("Debe detectar mutante con 2 secuencias")
    void testMutantVerticalAndAntiDiagonal() {
        String[] dnaSimple = {
                "ACGT",
                "CATT",
                "ATGT", // Antidiagonal T en T(3,0), T(2,1), T(1,2), T(0,3).
                "TCGT"  // Vertical T en T(0,3), T(1,3), T(2,3), T(3,3).
        };
        assertTrue(mutantDetector.isMutant(dnaSimple));
    }

    @Test
    @DisplayName("Debe ser HUMANO con solo 1 secuencia")
    void testHumanWithOnlyOneSequence() {
        String[] dna = {
                "AAAA", //Horizontal A, una unica secuencia de 4
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe ser HUMANO sin secuencias")
    void testHumanWithNoSequences() {
        String[] dna = {
                "ATGC", //No hay secuencias
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe fallar (ser humano) con matriz No Cuadrada")
    void testInvalidDnaNonSquare() {
        String[] dna = {
                "ATGC",
                "CAG", //Aqui falla y la matriz no es cuadrada
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe fallar (ser humano) con caracteres inválidos")
    void testInvalidDnaCharacters() {
        String[] dna = {
                "ATGX", //X no es un caracter valido
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe fallar (ser humano) con ADN nulo")
    void testNullDnaArray() {
        assertFalse(mutantDetector.isMutant(null));
    }

    @Test
    @DisplayName("Debe fallar (ser humano) con ADN vacío")
    void testEmptyDnaArray() {
        String[] dna = {};
        assertFalse(mutantDetector.isMutant(dna));
    }
}