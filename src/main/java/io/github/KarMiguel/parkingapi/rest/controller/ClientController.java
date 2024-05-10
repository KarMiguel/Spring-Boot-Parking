package io.github.KarMiguel.parkingapi.rest.controller;

import io.github.KarMiguel.parkingapi.entity.Client;
import io.github.KarMiguel.parkingapi.jwt.JwtUserDetails;
import io.github.KarMiguel.parkingapi.repository.projection.ClientProjection;
import io.github.KarMiguel.parkingapi.service.ClientService;
import io.github.KarMiguel.parkingapi.service.UserService;
import io.github.KarMiguel.parkingapi.rest.dto.PageableDTO;
import io.github.KarMiguel.parkingapi.rest.dto.client.ClientCreatedDTO;
import io.github.KarMiguel.parkingapi.rest.dto.client.ClientResponseDTO;
import io.github.KarMiguel.parkingapi.rest.dto.mapper.ClientMapper;
import io.github.KarMiguel.parkingapi.rest.dto.mapper.PageableMapper;
import io.github.KarMiguel.parkingapi.rest.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "Client",description = "Contém todas as operações relativas a um cliente")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    private final UserService userService;
    @Operation(summary = "Criar um novo cliente",
        description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado. " +
                "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                    content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ClientResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Cliente CPF já possui cadastro no sistema",
                    content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                    content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                    content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
    })

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClientResponseDTO>  create(
            @RequestBody @Valid ClientCreatedDTO dto,
            @AuthenticationPrincipal JwtUserDetails userDetails){
        Client client = ClientMapper.toClient(dto);
        client.setUser(userService.searchById(userDetails.getId()));
        clientService.saveClient(client);

        return ResponseEntity.status(201).body(ClientMapper.toDto(client));
    }
    @Operation(summary = "Localizar um cliente", description = "Recurso para localizar um cliente pelo ID. " +
        "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
        security = @SecurityRequirement(name = "security"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                    content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ClientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                    content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                    content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
        })

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDTO> getById(@PathVariable Long id){
        Client client = clientService.searchById(id);
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }

    @Operation(summary = "Recuperar dados do cliente autenticado",
            description = "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                @Parameter(in = QUERY,name = "page",
                        content = @Content(schema = @Schema(type = "integer",defaultValue = "0")),
                        description = "Representa página retonarda"

                ),
                @Parameter(in = QUERY,name = "size",
                        content = @Content(schema = @Schema(type = "integer",defaultValue = "20")),
                        description = "Representa total de elementos da página"
                ),
                @Parameter(in = QUERY,name = "sort",hidden = true,
                        content = @Content(schema = @Schema(type = "string",defaultValue = "id,asc")),
                        description = "Representa ordenação dos usuários.Aceita multiplos critérios de ordenação."
                )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ClientResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDTO> getAll(@Parameter(hidden = true)
                                              @PageableDefault(size = 5, sort = {"name"})
                                              Pageable pageable){
        Page <ClientProjection> clients = clientService.getList(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clients));
    }

    @Operation(summary = "Recuperar dados do cliente autenticado",
            description = "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ClientResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })

    @GetMapping("/details")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDTO> getDetails(@AuthenticationPrincipal JwtUserDetails userDetails){
        Client client = clientService.searchByUsername(userDetails.getId());
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }


}
