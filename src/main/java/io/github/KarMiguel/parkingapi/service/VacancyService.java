package io.github.KarMiguel.parkingapi.service;

import io.github.KarMiguel.parkingapi.entity.Vacancy;
import io.github.KarMiguel.parkingapi.exception.CodeUniqueViolationException;
import io.github.KarMiguel.parkingapi.exception.EntityNotFoundException;
import io.github.KarMiguel.parkingapi.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.github.KarMiguel.parkingapi.entity.Vacancy.StatusVacancy.FREE;

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

    @Transactional(readOnly = true)
    public Vacancy searchByCode(String code){
        return  vacancyRepository.findByCode(code).orElseThrow(
                ()-> new EntityNotFoundException(String.format("Vaga com código '%s' não foi encontrada",code))
        );
    }

    @Transactional(readOnly = true)
    public Vacancy searchByVacancyFree() {
        return vacancyRepository.findFirstByStatus(FREE).orElseThrow(
                () -> new EntityNotFoundException("Nenhuma vaga livre encontrada.")
        );
    }
}
