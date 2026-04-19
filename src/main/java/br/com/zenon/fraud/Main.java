package br.com.zenon.fraud;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static br.com.zenon.fraud.EnumTransactionType.CASH_OUT;
import static br.com.zenon.fraud.EnumTransactionType.PAYMENT;
import static java.lang.Math.min;

public class Main {

    static void main() throws IOException {

        printTransactions();
        readFirstNLines();
        readAndParseFirst10Lines();

    }

    private static void readAndParseFirst10Lines() {
        String FilePath = "data/PS_log.csv";
        try {
            List<Transaction> transactions = TransactionIngestor.read(FilePath);
            System.out.println("Primeiras 10 transações: ");
            for (int i = 0; i < min(10, transactions.size()); i++) {
                System.out.println(transactions.get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readFirstNLines() {
        String filePath = "data/PS_log.csv";
        int linhas = 1000;

        try {
            List<String> first1000Lines = TransactionIngestor.readFirstNLines(filePath, linhas);
            System.out.println("Lidas " + first1000Lines.size() + " linhas do arquivo");

            // Processa e imprime a última linha
            first1000Lines.stream()
                    .skip(first1000Lines.size() - 1)  // Pula para o último
                    .findFirst()
                    .ifPresent(lastLine -> System.out.println("Última linha: " + lastLine));

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

    private static void printTransactions() {
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
