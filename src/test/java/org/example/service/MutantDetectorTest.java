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
        // Se inicializa el objeto real para pruebas unitarias.
        mutantDetector = new MutantDetector();
    }

    // Casos Mutantes (debe retornar true)

    @Test
    @DisplayName("Horizontal + Diagonal (Mutante)")
    void testMutantWithHorizontalAndDiagonalSequences() {
        // Secuencia 1: CCCC horizontal (fila 4)
        // Secuencia 2: AAAA diagonal descendente (desde (0,0))
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(mutantDetector.isMutant(dna), "Debe detectar mutante con 2 secuencias (Horizontal CCCC y Diag AAAA).");
    }

    @Test
    @DisplayName("Verticales (Mutante)")
    void testMutantWithVerticalSequences() {
        // Secuencia 1: TTTT vertical (columna 0)
        // Secuencia 2: GGGG vertical (columna 1)
        String[] dna = {
                "TGTGAC",
                "TGTGAC",
                "TGTGAC",
                "TGTGAC",
                "AGTAGC",
                "CGTACC"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debe detectar 2 secuencias verticales.");
    }

    @Test
    @DisplayName("Múltiples Horizontales (Mutante)")
    void testMutantWithMultipleHorizontalSequences() {
        // Secuencia 1: TTTT horizontal (fila 0)
        // Secuencia 2: CCCC horizontal (fila 4)
        String[] dna = {
                "TTTTGA",
                "CAGTGC",
                "ATATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debe detectar 2 secuencias horizontales separadas.");
    }

    @Test
    @DisplayName("Diagonales Ascendentes y Descendentes (Mutante)")
    void testMutantWithBothDiagonals() {
        // Secuencia 1: AAAA descendente (↘) desde (0,0)
        // Secuencia 2: GGGG ascendente (↗) desde (5,0)
        String[] dna = {
                "AGTAGC", // A en (0,0)
                "GATGTC", // A en (1,2) <- debe ser A en (1,1) para ser diag
                "GTAGCC",
                "GCGTCA",
                "GTCCGA",
                "GCAATG" // G en (5,0)
        };
        // Usamos un ejemplo más claro que asegura 2 diagonales:
        String[] dnaDiagonales = {
                "ATGCAT", // A en (0,0)
                "AATACG", // A en (1,1)
                "GCAATC", // A en (2,2)
                "CAGAAC", // A en (3,3)
                "GCCCCG", // C en (4,1)
                "CGCGGG"  // GGGG ascendente desde (5,2)
        };
        assertTrue(mutantDetector.isMutant(dnaDiagonales), "Debe detectar secuencias en diagonal descendente y ascendente.");
    }

    @Test
    @DisplayName("Matriz Grande 10x10 (Mutante)")
    void testMutantWithLargeDna() {
        // Secuencia 1: TTTT horizontal (fila 0)
        // Secuencia 2: AAAA horizontal (fila 9)
        String[] dna = {
                "TTTTGAAAAA",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGATAA",
                "CCCCTACCCC",
                "TCACTGTCAC",
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AAAAGGGGGG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debe detectar mutante en matriz grande 10x10.");
    }

    @Test
    @DisplayName("Todo Igual (Mutante)")
    void testMutantAllSameCharacter() {
        // Múltiples secuencias en todas las direcciones
        String[] dna = {
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA",
                "AAAAAA"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debe detectar mutante si todos los caracteres son iguales.");
    }

    // Casos Humanos (debe retornar false)

    @Test
    @DisplayName("Solo 1 secuencia encontrada (Humano)")
    void testNotMutantWithOnlyOneSequence() {
        // Solo 1 secuencia: GGGG vertical (columna 2)
        String[] dna = {
                "ATGCTA",
                "CAGTGC",
                "GGGTAC",
                "GGGAAG",
                "GTACCC",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna), "No debe ser mutante con una sola secuencia.");
    }

    @Test
    @DisplayName("Sin secuencias (Humano)")
    void testNotMutantWithNoSequences() {
        // Ninguna secuencia de 4
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "No debe ser mutante si no hay secuencias.");
    }

    @Test
    @DisplayName("Matriz 4x4 sin secuencias (Humano)")
    void testNotMutantSmallDna() {
        // Ninguna secuencia de 4 en el tamaño mínimo
        String[] dna = {
                "GCTA",
                "TAGC",
                "ATCG",
                "GCAT"
        };
        assertFalse(mutantDetector.isMutant(dna), "No debe ser mutante en matriz 4x4 sin secuencias.");
    }

    // 3. Validaciones (debe retornar false)

    @Test
    @DisplayName("DNA null (Validación)")
    void testNotMutantWithNullDna() {
        assertFalse(mutantDetector.isMutant(null), "Debe manejar entrada nula y retornar false.");
    }

    @Test
    @DisplayName("Array vacío (Validación)")
    void testNotMutantWithEmptyDna() {
        String[] dna = {};
        assertFalse(mutantDetector.isMutant(dna), "Debe retornar false con array vacío.");
    }

    @Test
    @DisplayName("Matriz no cuadrada (4x5) (Validación)")
    void testNotMutantWithNonSquareDna() {
        // 4 filas, 5 columnas (no es 4x4)
        String[] dna = {
                "ATGC",
                "CAGTA", // Fila más larga
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "Debe retornar false si la matriz no es cuadrada.");
    }

    @Test
    @DisplayName("Carácter inválido 'X' (Validación)")
    void testNotMutantWithInvalidCharacters() {
        String[] dna = {
                "ATGX", // Carácter 'X' inválido
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "Debe retornar false con caracteres inválidos.");
    }

    @Test
    @DisplayName("14. Fila null (Validación)")
    void testNotMutantWithNullRow() {
        String[] dna = {
                "ATGC",
                "CAGT",
                null, // Fila nula
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "Debe retornar false si una fila es nula.");
    }

    @Test
    @DisplayName("Matriz muy pequeña (3x3) (Validación)")
    void testNotMutantWithTooSmallDna() {
        // Mínimo 4x4 para contener 4 secuencias.
        String[] dna = {
                "AAA",
                "AAA",
                "AAA"
        };
        assertFalse(mutantDetector.isMutant(dna), "Debe retornar false si la matriz es menor a 4x4.");
    }

    // 4. Edge Cases

    @Test
    @DisplayName("Secuencias de longitud 5 (Debe ser MUTANTE por solapamiento)")
    void testNotMutantWithSequenceLongerThanFour() {
        // La secuencia AAAAAG contiene dos patrones solapados: AAAA (0-3) y AAAA (1-4).
        // El algoritmo eficiente debe contar ambos y retornar TRUE.
        String[] dna = {
                "AAAAAG",
                "GCAATG",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "La secuencia AAAAAG genera 2 patrones solapados, por lo que el resultado debe ser TRUE.");
    }

    @Test
    @DisplayName("Diagonal en esquina (Mutante)")
    void testMutantDiagonalInCorner() {
        // Secuencia 1: AAAA diagonal que termina en la esquina (3,3)
        // Secuencia 2: GGGG horizontal (fila 4)
        String[] dna = {
                "AGGCGA",
                "TAATGC",
                "GTAATC",
                "CGACAA",
                "GGGGGT", // GGGG horizontal
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debe detectar mutante con una secuencia diagonal en la esquina y otra horizontal.");
    }
}