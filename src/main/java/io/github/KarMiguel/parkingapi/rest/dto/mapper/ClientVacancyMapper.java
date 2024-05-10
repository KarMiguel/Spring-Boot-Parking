package io.github.KarMiguel.parkingapi.rest.dto.mapper;

import io.github.KarMiguel.parkingapi.entity.ClientVacancy;
import io.github.KarMiguel.parkingapi.rest.dto.parking.ParkingCreateDTO;
import io.github.KarMiguel.parkingapi.rest.dto.parking.ParkingResponseDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientVacancyMapper {

    public static ClientVacancy toClientVacancy(ParkingCreateDTO dto){
        return new ModelMapper().map(dto,ClientVacancy.class);
    }

    public static ParkingResponseDTO toDto(ClientVacancy clientVacancy){
        return new ModelMapper().map(clientVacancy,ParkingResponseDTO.class);
    }

}
