package io.github.KarMiguel.parkingapi.service;

import io.github.KarMiguel.parkingapi.entity.ClientVacancy;
import io.github.KarMiguel.parkingapi.exception.EntityNotFoundException;
import io.github.KarMiguel.parkingapi.repository.ClientVacancyRepository;
import io.github.KarMiguel.parkingapi.repository.projection.ClientVacancyProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientVacancyService {

    private final ClientVacancyRepository repository;


    @Transactional
    public ClientVacancy save(ClientVacancy clientVacancy){
        return repository.save(clientVacancy);
    }

    @Transactional(readOnly = true)
    public ClientVacancy searchByReceipt(String receipt) {
        return repository.findByReceiptAndDateExitIsNull(receipt).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format("Recibo '%s' não econtrado no Sistema ou check-out já realizado.",receipt)
                )
        );
    }

    @Transactional(readOnly = true)
    public long getTotalParkingTimesFull(String cpf) {
        return repository.countByClientCpfAndDateExitIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClientVacancyProjection> searchAllByClientCpf(String cpf, Pageable pageable) {
        return repository.findAllByClientCpf(cpf,pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClientVacancyProjection> searchAllByClientId(Long id, Pageable pageable) {
        return repository.findAllByClientUserId(id,pageable);
    }
}
