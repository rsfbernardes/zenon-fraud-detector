package br.com.zenon.fraud;

import java.math.BigDecimal;

public record Transaction(
        long step,
        EnumTransactionType type,
        BigDecimal amount,
        String nameOrig,
        BigDecimal oldbalanceOrg,
        BigDecimal newbalanceOrig,
        String nameDest,
        BigDecimal oldbalanceDest,
        BigDecimal newbalanceDest,
        int isFraud,
        int isFlaggedFraud
) {}
