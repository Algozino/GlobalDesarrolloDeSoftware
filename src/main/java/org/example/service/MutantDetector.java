package org.example.service;

import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4; // 4 letras iguales
    private static final int MIN_SEQUENCES_FOR_MUTANT = 2; // Más de una
    private static final int MIN_MATRIX_SIZE = 4;

    // set para validacion O(1) de caracteres
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');





    //metodo público principal.

    public boolean isMutant(String[] dna) {
        // validacion de entrada
        if (!isValidDnaInput(dna)) {
            return false;
        }

        final int N = dna.length;
        int sequenceCount = 0;

        // conversion a char[][] (Optimización de acceso)
        char[][] matrix = dnaToMatrix(dna, N);

        // single pass (optimizacion de recorrido)
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {

                // boundary checking (optimización de limites)

                // horizontal
                if (col <= N - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                    }
                }

                //vertical
                if (row <= N - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                    }
                }

                // diagonal (↘)
                if (row <= N - SEQUENCE_LENGTH && col <= N - SEQUENCE_LENGTH) {
                    if (checkDiagonal(matrix, row, col)) {
                        sequenceCount++;
                    }
                }

                // diagonal Inversa (↗)
                if (row >= SEQUENCE_LENGTH - 1 && col <= N - SEQUENCE_LENGTH) {
                    if (checkAntiDiagonal(matrix, row, col)) {
                        sequenceCount++;
                    }
                }

                // early termination (optimizacion critica que plantea la guia)
                // Si ya encontramos 2 o más, no seguimos buscando.
                if (sequenceCount >= MIN_SEQUENCES_FOR_MUTANT) {
                    return true;
                }
            }
        }

        // si termina el loop y no llego a 2, es humano.
        return false;
    }


     // validador de la entrada.

    private boolean isValidDnaInput(String[] dna) {
        if (dna == null || dna.length < MIN_MATRIX_SIZE) {
            return false; // Matriz nula o demasiado pequeña
        }
        final int N = dna.length;
        for (String row : dna) {
            if (row == null || row.length() != N) {
                return false; // No es NxN
            }
            for (char c : row.toCharArray()) {
                if (!VALID_BASES.contains(c)) {
                    return false; // Caracter inválido
                }
            }
        }
        return true;
    }





     // conversion a char[][] para acceso O(1)

    private char[][] dnaToMatrix(String[] dna, int N) {
        char[][] matrix = new char[N][N];
        for (int i = 0; i < N; i++) {
            matrix[i] = dna[i].toCharArray();
        }
        return matrix;
    }





    // Métodos de chequeo con Comparación Directa

    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
                matrix[row + 2][col] == base &&
                matrix[row + 3][col] == base;
    }

    private boolean checkDiagonal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
                matrix[row + 2][col + 2] == base &&
                matrix[row + 3][col + 3] == base;
    }

    private boolean checkAntiDiagonal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base &&
                matrix[row - 2][col + 2] == base &&
                matrix[row - 3][col + 3] == base;
    }
}
