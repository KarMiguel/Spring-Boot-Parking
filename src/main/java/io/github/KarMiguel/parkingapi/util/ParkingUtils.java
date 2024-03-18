package io.github.KarMiguel.parkingapi.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

    public static String generateReceipt(){
        LocalDateTime date = LocalDateTime.now();
        String receipt = date.toString().substring(0,19);
        return receipt.replace("-","")
                .replace(":","")
                .replace("T","-");
    }

    private static final double PRIMEIROS_15_MINUTES = 5.00;
    private static final double PRIMEIROS_60_MINUTES = 9.25;
    private static final double ADICIONAL_15_MINUTES = 1.75;
    public static BigDecimal calculateCost(LocalDateTime entrada, LocalDateTime saida) {
        long minutes = entrada.until(saida, ChronoUnit.MINUTES);
        double total = 0.0;

        if (minutes <= 15) {
            total = PRIMEIROS_15_MINUTES;
        } else if (minutes <= 60) {
            total = PRIMEIROS_60_MINUTES;
        } else {
            long minutesBeyond60 = minutes - 60 ;
            int additionalBlocks = (int) Math.ceil((double) minutesBeyond60 / 15);
            total = PRIMEIROS_60_MINUTES + (ADICIONAL_15_MINUTES * additionalBlocks);
        }
        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    private static final double DESCONTO_PERCENTUAL = 0.30;

    public static BigDecimal calculateDiscount(BigDecimal custo, long numeroDeVezes) {

        BigDecimal desconto = BigDecimal.ZERO;

        if (numeroDeVezes % 10 == 0 && numeroDeVezes != 0){
            desconto = custo.multiply(BigDecimal.valueOf(DESCONTO_PERCENTUAL));
        }
        return desconto.setScale(2, RoundingMode.HALF_EVEN);
    }
}
