package io.github.KarMiguel.parkingapi.service;

import io.github.KarMiguel.parkingapi.entity.Client;
import io.github.KarMiguel.parkingapi.exception.CpfUniqueViolationException;
import io.github.KarMiguel.parkingapi.exception.EntityNotFoundException;
import io.github.KarMiguel.parkingapi.repository.ClientRepository;
import io.github.KarMiguel.parkingapi.repository.projection.ClientProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public Client saveClient(Client client){
        try {
            return clientRepository.save(client);
        }catch (DataIntegrityViolationException ex){
            throw new CpfUniqueViolationException(String.format("CPF '%s' ja cadastrado no sistema",client.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Client searchById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Cliente id = %s não encontrada no sistema",id)));
    }

    @Transactional(readOnly = true)
    public Page<ClientProjection> getList(Pageable pageable) {
        return clientRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Client searchByUsername(Long id) {
        return clientRepository.findByUserId(id);
    }
    @Transactional(readOnly = true)
    public Client searchByCpf(String cpf) {
        return clientRepository.findByCpf(cpf).orElseThrow(
                () -> new jakarta.persistence.EntityNotFoundException(String.format("Cliente com CPF  '%s' não encontrado.",cpf))
        );
    }
}
