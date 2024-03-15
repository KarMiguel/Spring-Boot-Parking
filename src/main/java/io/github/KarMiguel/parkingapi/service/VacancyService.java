package io.github.KarMiguel.parkingapi.service;

import io.github.KarMiguel.parkingapi.entity.Vacancy;
import io.github.KarMiguel.parkingapi.exception.CodeUniqueViolationException;
import io.github.KarMiguel.parkingapi.exception.EntityUserNotFoundException;
import io.github.KarMiguel.parkingapi.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VacancyService {


    private final VacancyRepository vacancyRepository;

    @Transactional
    public Vacancy save(Vacancy vacancy){
        try {
            return vacancyRepository.save(vacancy);
        }catch (DataIntegrityViolationException ex){
            throw new CodeUniqueViolationException(String.format("Vaga com código '%s' já cadastrado!",vacancy.getId()));
        }

    }

    public Vacancy searchByCode(String code){
        return  vacancyRepository.findByCode(code).orElseThrow(
                ()-> new EntityUserNotFoundException(String.format("Vaga com código '%s' não foi encontrada",code))
        );
    }

}
