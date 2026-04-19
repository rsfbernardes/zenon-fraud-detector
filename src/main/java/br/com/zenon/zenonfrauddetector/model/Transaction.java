package br.com.zenon.zenonfrauddetector.model;

import br.com.zenon.zenonfrauddetector.enums.TransactionType;

import java.math.BigDecimal;

public record Transaction(
        long step,
        TransactionType type,
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
