package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) // Se usara en el campo dna
@Retention(RetentionPolicy.RUNTIME) // Se chequeara en tiempo de ejecución
@Constraint(validatedBy = ValidDnaSequenceValidator.class) // La clase que hace la lógica

public @interface ValidDnaSequence {
    String message() default "Secuencia de ADN inválida. Debe ser una matriz NxN (N>=4) y contener solo los caracteres A, T, C, G.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}