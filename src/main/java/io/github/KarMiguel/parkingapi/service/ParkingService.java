package io.github.KarMiguel.parkingapi.service;

import io.github.KarMiguel.parkingapi.entity.Client;
import io.github.KarMiguel.parkingapi.entity.ClientVacancy;
import io.github.KarMiguel.parkingapi.entity.Vacancy;
import io.github.KarMiguel.parkingapi.util.ParkingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ClientVacancyService clientVacancyService;
    private final ClientService clientService;
    private final VacancyService vacancyService;

    public ClientVacancy checkIn(ClientVacancy clientVacancy){
        Client client = clientService.searchByCpf(clientVacancy.getClient().getCpf());
        clientVacancy.setClient(client);

        Vacancy vacancy = vacancyService.searchByVacancyFree();
        vacancy.setStatus(Vacancy.StatusVacancy.BUSY);
        clientVacancy.setVacancy(vacancy);

        clientVacancy.setDateEntry(LocalDateTime.now());

        clientVacancy.setReceipt(ParkingUtils.generateReceipt());

        return clientVacancyService.save(clientVacancy);
    }


    @Transactional
    public ClientVacancy ckeckout(String receipt) {
        ClientVacancy clientVacancy = clientVacancyService.searchByReceipt(receipt);

        LocalDateTime dateDeparture = LocalDateTime.now();

        BigDecimal price = ParkingUtils.calculateCost(clientVacancy.getDateCreated(),dateDeparture);
        clientVacancy.setPrice(price);

        long totalNumberOfTimes = clientVacancyService.getTotalParkingTimesFull(clientVacancy.getClient().getCpf());

        BigDecimal discount =  ParkingUtils.calculateDiscount(price,totalNumberOfTimes);
        clientVacancy.setDiscount(discount);
        clientVacancy.setTotal(price.subtract(discount));
        clientVacancy.setDateExit(dateDeparture);
        clientVacancy.getVacancy().setStatus(Vacancy.StatusVacancy.FREE);

        return clientVacancyService.save(clientVacancy);
    }
}
