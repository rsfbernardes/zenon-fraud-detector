package br.com.zenon.fraud;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static br.com.zenon.fraud.EnumTransactionType.CASH_OUT;
import static br.com.zenon.fraud.EnumTransactionType.PAYMENT;
import static java.lang.Math.min;

public class Main {

    private static final TransactionListRepositoryImpl transactionListRepositoryImpl = new TransactionListRepositoryImpl();


    static void main() {

        printTransactions();
        System.out.println();
        System.out.println("=====================================");
        System.out.println();
        readFirstNLines();
        System.out.println();
        System.out.println("=====================================");
        System.out.println();
        readParseAndPrintFirst10Lines();
        System.out.println();
        System.out.println("=====================================");
        System.out.println();
        readFileWithBadData();
        System.out.println();
        System.out.println("=====================================");
        System.out.println();
        fraudAnalyzer();
        System.out.println("=====================================");
        System.out.println();
        findByOriginName("C1231006815");
        System.out.println();
        findByOriginName("C12345");

        System.out.println();
        findLastByOriginName();
        System.out.println();
        findByOriginNameUsingMap();
    }

    private static void findLastByOriginName() {
        TransactionIngestor.ParseResult parseResult = readAndParseFirst100000Lines();
        var start = System.nanoTime();
       Optional<Transaction> transactionOptional = transactionListRepositoryImpl.findLastByOriginName("C1868032458", parseResult.transactions());
        var end = System.nanoTime();
        System.out.println((end - start)/1_000_000.0 + " ms using list");
       if (transactionOptional.isPresent()) {
           System.out.println(transactionOptional.get());
       } else {
           System.out.println("No transactions found");
       }
    }

    private static void findByOriginNameUsingMap() {
        TransactionIngestor.ParseResult parseResult = readAndParseFirst100000Lines();
        long start = System.nanoTime();
        Optional<Transaction> transactionOptional = transactionListRepositoryImpl.findByOriginNameUsingMap("C1868032458", parseResult.transactions());
        long end = System.nanoTime();
        System.out.println((end - start)/1_000_000.0 + " ms using map");
        if (transactionOptional.isPresent()) {
            System.out.println(transactionOptional.get());
        } else {
            System.out.println("No transactions found");
        }
    }

    private static void findByOriginName(String originName) {
        TransactionIngestor.ParseResult parseResult = readAndParseFirst100000Lines();
        Optional<Transaction> transactionOptional = transactionListRepositoryImpl.findByOriginName(originName, parseResult.transactions());
        if (transactionOptional.isPresent()) {
            System.out.println(transactionOptional.get());
        } else {
            System.out.println("Transaction not found for client: " + originName);
        }
    }

    private static void fraudAnalyzer() {
        String filePath = "data/PS_log.csv";
        try {
            TransactionIngestor.ParseResult result = TransactionIngestor.read(filePath, 50000);
            FraudAnalyzer analyzer = new FraudAnalyzer(result.transactions());
            analyzer.printFraudReport();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readFileWithBadData() {
        String filePath = "data/paysim_with_bad_data.csv";
        try {
            TransactionIngestor.ParseResult result = TransactionIngestor.read(filePath);
            result.errors().forEach(System.out::println);
            System.out.println(result.transactions().size());
            result.transactions().forEach(System.out::println);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void readParseAndPrintFirst10Lines() {
        String filePath = "data/PS_log.csv";
        try {
            TransactionIngestor.ParseResult result = TransactionIngestor.read(filePath);
            System.out.println("First " + 10 + "transactions: ");
            for (int i = 0; i < min(10, result.transactions().size()); i++) {
                System.out.println(result.transactions().get(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static TransactionIngestor.ParseResult readAndParseFirst100000Lines() {
        String filePath = "data/PS_log.csv";
        try {
            return TransactionIngestor.read(filePath);
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

            first1000Lines.stream()
                    .skip(first1000Lines.size() - 1)
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
                false,
                false
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
                true,
                false
        );
        System.out.println(transaction1);
        System.out.println(transaction2);
    }
}
