package io.github.KarMiguel.parkingapi.rest.controller;

import io.github.KarMiguel.parkingapi.jwt.JwtToken;
import io.github.KarMiguel.parkingapi.jwt.JwtUserDetailsService;
import io.github.KarMiguel.parkingapi.rest.dto.users.UserLoginDTO;
import io.github.KarMiguel.parkingapi.rest.dto.users.UserResponseDTO;
import io.github.KarMiguel.parkingapi.rest.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication",description = "Recurso para proceder com autenticação da api")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1")
public class AuthenticationController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Autenticar API",description = "Recurso de autenticação na API",
        security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200",description = "Autenticação realizada com sucesso e retorno com token!",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDTO.class ))),
                    @ApiResponse(responseCode = "400",description = "Credenciais Invalidas.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class ))),
                    @ApiResponse(responseCode = "422",description = "Campo(s) invalido(s).",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorMessage.class )))

            })

    @PostMapping("/auth")

    public ResponseEntity<?> authentication(@RequestBody @Valid UserLoginDTO dto, HttpServletRequest request){
        log.info("Processo autenticação pelo login {}",dto.getUsername());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword());

            authenticationManager.authenticate(authenticationToken);

            JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());

            return ResponseEntity.ok(token);
        }catch (AuthenticationException ex){
            log.warn("Bad Credentiais from username {}",dto.getUsername());
        }
        return ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST,"Credenciais Inválidas"));
    }


}
