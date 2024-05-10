package io.github.KarMiguel.parkingapi.rest.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class ClientCreatedDTO {

    @NotBlank
    @Size(min = 3,max = 100)
    private String name;

    @NotBlank
    @Size(min = 11,max = 11)
    @CPF
    private String cpf;
}
