package io.github.KarMiguel.parkingapi.web.controller;

import io.github.KarMiguel.parkingapi.jwt.JwtToken;
import io.github.KarMiguel.parkingapi.jwt.JwtUserDetails;
import io.github.KarMiguel.parkingapi.jwt.JwtUserDetailsService;
import io.github.KarMiguel.parkingapi.web.dto.UserLoginDTO;
import io.github.KarMiguel.parkingapi.web.exception.ErrorMessage;
import io.jsonwebtoken.Jwt;
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

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1")
public class AuthenticationController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

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
