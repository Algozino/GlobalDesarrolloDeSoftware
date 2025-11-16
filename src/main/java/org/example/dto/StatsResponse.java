package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "Estadísticas de verificaciones de ADN")
public class StatsResponse {

    @Schema(description = "Cantidad de ADN mutante verificado", example = "40")
    private long count_mutant_dna;

    @Schema(description = "Cantidad de ADN humano verificado", example = "100")
    private long count_human_dna;

    @Schema(description = "Ratio: mutantes / humanos", example = "0.4")
    private double ratio;
}