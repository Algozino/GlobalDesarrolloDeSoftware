package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.DnaRequest;
import org.example.dto.ErrorResponse;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant API", description = "API para detección de mutantes y estadísticas")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    @Operation(summary = "Verificar si un ADN es mutante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es mutante (OK)"),
            @ApiResponse(responseCode = "403", description = "No es mutante (Forbidden)"),
            @ApiResponse(responseCode = "400", description = "ADN inválido (Bad Request)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {
        // La validacion @ValidDnaSequence se ejecuta automaticamente
        boolean isMutant = mutantService.analyzeDna(request.getDna());

        return isMutant
                ? ResponseEntity.ok().build() // 200 OK
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
    }

    @GetMapping("/stats")
    @Operation(summary = "Obtiene estadísticas de verificaciones de ADN")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StatsResponse.class)))
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
