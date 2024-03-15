package io.github.KarMiguel.parkingapi.web.dto.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientResponseDTO {

    private Long id;
    private String name;
    private String cpf;
}
