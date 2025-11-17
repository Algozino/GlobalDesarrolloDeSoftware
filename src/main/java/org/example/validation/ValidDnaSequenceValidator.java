package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;


public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> { // Esta clase implementa la lógica para @ValidDnaSequence

    // Compilamos el patron una sola vez para eficiencia
    private static final Pattern DNA_PATTERN = Pattern.compile("^[ATCG]+$");
    private static final int MIN_SIZE = 4; // 4x4 es el minimo para una secuencia de 4

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length == 0) {
            // @NotNull y @NotEmpty se encargan de esto pero por las dudas lo sumo
            return false;
        }

        final int N = dna.length;

        // Validar tamano minimo (como dice la guia)
        if (N < MIN_SIZE) {
            return false;
        }

        // Validar que sea NxN y caracteres validos
        for (String row : dna) {
            //Validar que la fila no sea nula y tenga longitud N
            if (row == null || row.length() != N) {
                return false;
            }
            // Validar caracteres (A, T, C, G)
            if (!DNA_PATTERN.matcher(row).matches()) {
                return false;
            }
        }

        return true; // pasa todas las validaciones
    }
}