package io.github.KarMiguel.parkingapi.web.controller;

import io.github.KarMiguel.parkingapi.entity.Vacancy;
import io.github.KarMiguel.parkingapi.service.VacancyService;
import io.github.KarMiguel.parkingapi.web.dto.mapper.VacancyMapper;
import io.github.KarMiguel.parkingapi.web.dto.vacancy.VacancyCreatedDTO;
import io.github.KarMiguel.parkingapi.web.dto.vacancy.VacancyResponseDTO;
import io.github.KarMiguel.parkingapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vacancies")
public class VacancyController {

    private final VacancyService vacancyService;

    @Operation(summary = "Criar uma nova vaga", description = "Recurso para criar uma nova vaga." +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado")),
                    @ApiResponse(responseCode = "409", description = "Vaga já cadastrada",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> created(@RequestBody @Valid VacancyCreatedDTO dto){
        Vacancy vacancy = VacancyMapper.toVacancy(dto);
        vacancyService.save(vacancy);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{code}")
                .buildAndExpand(vacancy.getCode())
                .toUri();
        return ResponseEntity.created(location).build();
    }
    @Operation(summary = "Localizar uma vaga", description = "Recurso para retornar uma vaga pelo seu código" +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = VacancyResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Vaga não localizada",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VacancyResponseDTO> getByCode(@PathVariable String code) {
        Vacancy vacancy = vacancyService.searchByCode(code);
        return ResponseEntity.ok(VacancyMapper.toDto(vacancy));
    }

    @Operation(summary = "Atualizar uma vaga", description = "Recurso para atualizar uma vaga existente." +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Vaga atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Vaga não encontrada",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })

    @PutMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable String code, @RequestBody @Valid VacancyCreatedDTO dto){
        Vacancy existingVacancy = vacancyService.searchByCode(code);
        if(existingVacancy == null) {
            return ResponseEntity.notFound().build();
        }
        Vacancy updatedVacancy = VacancyMapper.toVacancy(dto);
        updatedVacancy.setCode(code);
        vacancyService.save(updatedVacancy);
        return ResponseEntity.noContent().build();
    }

}
