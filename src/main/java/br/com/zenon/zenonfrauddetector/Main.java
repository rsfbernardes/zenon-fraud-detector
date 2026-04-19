package br.com.zenon.zenonfrauddetector;

import br.com.zenon.zenonfrauddetector.model.Transaction;

import java.math.BigDecimal;

import static br.com.zenon.zenonfrauddetector.enums.TransactionType.CASH_OUT;
import static br.com.zenon.zenonfrauddetector.enums.TransactionType.PAYMENT;

public class Main {

    static void main() {

        var transaction1 = new Transaction(
                1,
                PAYMENT,
                BigDecimal.valueOf(9839.64),
                "C1231006815",
                BigDecimal.valueOf(170136.0),
                BigDecimal.valueOf(160296.36),
                "M1979787155",
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0,
                0
        );

        var transaction2 = new Transaction(
                743,
                CASH_OUT,
                BigDecimal.valueOf(850002.52),
                "C1280323807",
                BigDecimal.valueOf(850002.52),
                BigDecimal.ZERO,
                "C873221189",
                BigDecimal.valueOf(6510099.11),
                BigDecimal.valueOf(7360101.63),
                1,
                0
        );

        System.out.println(transaction1);
        System.out.println(transaction2);

    }
}
