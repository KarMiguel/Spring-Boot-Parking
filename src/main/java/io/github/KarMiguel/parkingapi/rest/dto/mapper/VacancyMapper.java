package io.github.KarMiguel.parkingapi.rest.dto.mapper;


import io.github.KarMiguel.parkingapi.entity.Vacancy;
import io.github.KarMiguel.parkingapi.rest.dto.vacancy.VacancyCreatedDTO;
import io.github.KarMiguel.parkingapi.rest.dto.vacancy.VacancyResponseDTO;
import lombok.*;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class VacancyMapper {

    public  static Vacancy toVacancy(VacancyCreatedDTO dto){
        return  new ModelMapper().map(dto,Vacancy.class);
    }

    public  static VacancyResponseDTO toDto(Vacancy vacancy){
        return  new ModelMapper().map(vacancy,VacancyResponseDTO.class);
    }
}
