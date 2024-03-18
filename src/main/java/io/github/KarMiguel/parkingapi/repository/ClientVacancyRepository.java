package io.github.KarMiguel.parkingapi.repository;

import io.github.KarMiguel.parkingapi.entity.ClientVacancy;
import io.github.KarMiguel.parkingapi.repository.projection.ClientVacancyProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientVacancyRepository extends JpaRepository<ClientVacancy,Long> {


    Optional<ClientVacancy> findByReceiptAndDateExitIsNull(String receipt);

    long countByClientCpfAndDateExitIsNotNull(String cpf);

    Page<ClientVacancyProjection> findAllByClientCpf(String cpf, Pageable pageable);

    Page<ClientVacancyProjection> findAllByClientUserId(Long id, Pageable pageable);
}
