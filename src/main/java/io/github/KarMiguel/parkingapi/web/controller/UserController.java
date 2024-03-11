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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User",description = "Contém  todas as operações relaticas  aos recursos de cadastro, edição e leitura de um usuário.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Criar novo Usuário",description = "Recurso para criar novo Usuário.",
    responses = {
            @ApiResponse(responseCode = "201",description = "Usuário já criado com Sucesso!",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =UserResponseDTO.class ))),
            @ApiResponse(responseCode = "409",description = "Usuário/e-email já cadastrado no sistema.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class ))),
            @ApiResponse(responseCode = "422",description = "Recurso não processados por dados de entrda inválidos.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class )))

    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> created(@RequestBody @Valid UserCreatedDTO dto) throws UsernameUniqueViolationException {
        Users user = userService.save(UserMapper.toUser(dto));
        return  ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDto(user));
    }

    @Operation(summary = "Recuperar um usuario por Id",description = "Recuperar um usuario por Id.",
            responses = {
                    @ApiResponse(responseCode = "200",description = "Recurso recuperado com Sucesso!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation =UserResponseDTO.class ))),
                    @ApiResponse(responseCode = "404",description = "Recurso não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class )))

            })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Long id){
        Users newUser = userService.searchById(id);
        return  ResponseEntity.ok(UserMapper.toDto(newUser));
    }

    @Operation(summary = "Atualizar Senha",description = "Atualizar Senha.",
            responses = {
                    @ApiResponse(responseCode = "204",description = "Senha atualizada com Sucesso!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Void.class ))),
                    @ApiResponse(responseCode = "404",description = "Recurso não encontrado.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class ))),
                    @ApiResponse(responseCode = "400",description = "Senha não confere.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class )))


            })


    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDTO dto){
        Users newUser = userService.updatePassword(id, dto.getCurrentPassword(),dto.getNewPassword(),dto.getConfirmPassword());
        return  ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        List<Users> users = userService.listAll();
        return  ResponseEntity.ok(UserMapper.toListDto(users));
    }

}
