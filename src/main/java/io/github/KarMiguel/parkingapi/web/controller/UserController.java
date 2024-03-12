package io.github.KarMiguel.parkingapi.web.controller;

import io.github.KarMiguel.parkingapi.entity.Users;
import io.github.KarMiguel.parkingapi.exception.UsernameUniqueViolationException;
import io.github.KarMiguel.parkingapi.service.UserService;
import io.github.KarMiguel.parkingapi.web.dto.UserCreatedDTO;
import io.github.KarMiguel.parkingapi.web.dto.UserPasswordDTO;
import io.github.KarMiguel.parkingapi.web.dto.UserResponseDTO;
import io.github.KarMiguel.parkingapi.web.dto.mapper.UserMapper;
import io.github.KarMiguel.parkingapi.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User",description = "Contém  todas as operações relaticas  aos recursos de cadastro, edição e leitura de um usuário.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Criar um novo usuário", description = "Recurso para criar um novo usuário",
        responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))

    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> created(@RequestBody @Valid UserCreatedDTO dto) throws UsernameUniqueViolationException {
        Users user = userService.save(UserMapper.toUser(dto));
        return  ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
    }

    @Operation(summary = "Recuperar um usuario por Id",description = "Requisição exige um Bearer Token.Acesso retrito ADMIN|CLIENTE",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200",description = "Recurso recuperado com Sucesso!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation =UserResponseDTO.class ))),
                    @ApiResponse(responseCode = "403",description = "Usuário sem permissão.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class ))),
                    @ApiResponse(responseCode = "404",description = "Recurso não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class )))

            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id){
        Users newUser = userService.searchById(id);
        return  ResponseEntity.ok(UserMapper.toDto(newUser));
    }

    @Operation(summary = "Atualizar Senha",description = "Requisição exige um Bearer Token.Acesso retrito ADMIN|CLIENTE",
        security = @SecurityRequirement(name = "security"),
            responses = {
                @ApiResponse(responseCode = "204",description = "Senha atualizada com Sucesso!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = Void.class ))),
                @ApiResponse(responseCode = "400",description = "Senha não confere.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorMessage.class )))
                @ApiResponse(responseCode = "403",description = "Usuário sem permissão.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorMessage.class ))),
                @ApiResponse(responseCode = "404",description = "Recurso não encontrado.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorMessage.class ))),

            })


    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN' , 'CLIENTE') AND ( #id == authentication.principal.id)")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDTO dto){
        Users newUser = userService.updatePassword(id, dto.getCurrentPassword(),dto.getNewPassword(),dto.getConfirmPassword());
        return  ResponseEntity.noContent().build();
    }


    @Operation(summary = "Listar Usuário cadastrados",description = "Requisição exige um Bearer Token.Acesso retrito ADMIN",
        security = @SecurityRequirement(name = "security"),
            responses = {
                @ApiResponse(responseCode = "200",description = "Lista  com todos usuarios cadastrados!!",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = Void.class ))),
                @ApiResponse(responseCode = "403",description = "Usuário sem permissão.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErrorMessage.class ))),
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        List<Users> users = userService.listAll();
        return  ResponseEntity.ok(UserMapper.toListDto(users));
    }

}
