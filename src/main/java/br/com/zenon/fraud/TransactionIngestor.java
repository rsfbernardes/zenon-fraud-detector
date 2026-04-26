package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TransactionIngestor {

    public record ParseResult(List<Transaction> transactions, List<String> errors) {
    }

    public static ParseResult read(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return parse(sb.toString());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            throw e;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            throw e;
        }
    }

    public static ParseResult read(String filePath, int lines) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null && lines-- > 0) {
                sb.append(line).append("\n");
            }
            return parse(sb.toString());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            throw e;
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            throw e;
        }
    }

    public static List<String> readFirstNLines(String filePath, int n) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null && count < n) {
                lines.add(line);
                count++;
            }
        }
        return lines;
    }

    static ParseResult parse(String content) {
        List<Transaction> transactions = new ArrayList<>();
        java.util.Set<String> errorsSet = new java.util.LinkedHashSet<>();

        content.lines()
                .skip(1)
                .forEach(line -> {
                    try {
                        String[] fields = line.split(",", -1);
                        if (fields.length != 11) {
                            throw new IllegalArgumentException("Expected 11 fields but got " + fields.length);
                        }
                        for (int i = 0; i < fields.length; i++) {
                            if (fields[i] != null) fields[i] = fields[i].trim();
                        }

                        long step = Long.parseLong(Objects.requireNonNull(fields[0]));
                        if (step <= 0) {
                            throw new IllegalArgumentException("Step should be positive: " + step);
                        }

                        EnumTransactionType type;
                        try {
                            type = EnumTransactionType.valueOf(fields[1]);
                        } catch (IllegalArgumentException ex) {
                            throw new IllegalArgumentException("No enum constant br.com.zenon.fraud.TransactionType." + fields[1]);
                        }

                        if (fields[2] == null || fields[2].isEmpty()) {
                            throw new NumberFormatException();
                        }

                        BigDecimal amount = new BigDecimal(fields[2]);
                        if (amount.compareTo(BigDecimal.ZERO) < 0) {
                            throw new IllegalArgumentException("Amount should be positive: " + amount.toPlainString());
                        }

                        if (fields[3] == null || fields[3].isBlank()) {
                            throw new IllegalArgumentException("Name should not be empty");
                        }

                        BigDecimal oldBalanceOrig = new BigDecimal(fields[4]);
                        if (oldBalanceOrig.compareTo(BigDecimal.ZERO) < 0) {
                            throw new IllegalArgumentException("OldBalance should be positive: " + oldBalanceOrig.toPlainString());
                        }

                        BigDecimal newBalanceOrig = new BigDecimal(fields[5]);
                        if (newBalanceOrig.compareTo(BigDecimal.ZERO) < 0) {
                            throw new IllegalArgumentException("NewBalance should be positive: " + newBalanceOrig.toPlainString());
                        }

                        String nameDest = fields[6];

                        BigDecimal oldBalanceDest = new BigDecimal(fields[7]);
                        if (oldBalanceDest.compareTo(BigDecimal.ZERO) < 0) {
                            throw new IllegalArgumentException("OldBalance should be positive: " + oldBalanceDest.toPlainString());
                        }

                        BigDecimal newBalanceDest = new BigDecimal(fields[8]);
                        if (newBalanceDest.compareTo(BigDecimal.ZERO) < 0) {
                            throw new IllegalArgumentException("NewBalance should be positive: " + newBalanceDest.toPlainString());
                        }

                        int isFraud = Integer.parseInt(fields[9]);
                        int isFlaggedFraud = Integer.parseInt(fields[10]);

                        Transaction transaction = new Transaction(
                                step,
                                type,
                                amount,
                                fields[3],
                                oldBalanceOrig,
                                newBalanceOrig,
                                nameDest,
                                oldBalanceDest,
                                newBalanceDest,
                                isFraud,
                                isFlaggedFraud
                        );
                        transactions.add(transaction);
                    } catch (Exception e) {
                        errorsSet.add("Error: " + line + " | " + e);
                    }
                });
        List<String> errors = new ArrayList<>(errorsSet);
        return new ParseResult(transactions, errors);
    }
}
