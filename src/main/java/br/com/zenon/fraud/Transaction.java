package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(
        long step,
        EnumTransactionType type,
        BigDecimal amount,
        String nameOrig,
        BigDecimal oldBalanceOrig,
        BigDecimal newBalanceOrig,
        String nameDest,
        BigDecimal oldBalanceDest,
        BigDecimal newBalanceDest,
        boolean isFraud,
        boolean isFlaggedFraud
) {}
