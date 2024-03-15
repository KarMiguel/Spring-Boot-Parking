package io.github.KarMiguel.parkingapi.web.dto.mapper;

import io.github.KarMiguel.parkingapi.entity.Client;
import io.github.KarMiguel.parkingapi.web.dto.client.ClientCreatedDTO;
import io.github.KarMiguel.parkingapi.web.dto.client.ClientResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {
    public static Client toClient(ClientCreatedDTO dto){
        return new ModelMapper().map(dto,Client.class);
    }

    public static ClientResponseDTO toDto(Client client){
        return new ModelMapper().map(client, ClientResponseDTO.class);
    }
}
