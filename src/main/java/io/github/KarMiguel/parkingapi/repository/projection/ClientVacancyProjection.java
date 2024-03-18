package io.github.KarMiguel.parkingapi.repository.projection;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClientVacancyProjection {

    String getPlate();
    String getBrand();
    String getModel();
    String getColor();
    String getClientCpf();
    String getReceipt();
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDateEntry();
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDateExit();
    String getVacancyCode();
    BigDecimal getPrice();
    BigDecimal getDiscount();
}
