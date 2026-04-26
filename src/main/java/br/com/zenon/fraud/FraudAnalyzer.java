package br.com.zenon.fraud;

import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FraudAnalyzer {

    private final List<Transaction> transactions;

    public FraudAnalyzer(List<Transaction> transactions) {
        this.transactions = Objects.requireNonNull(transactions);
    }

    public void printFraudReport() {
        List<Transaction> fraudTransactions = getAllFraudTransactions();

        printTotalFraud(fraudTransactions.size());
        print3MajorValueFrauds(fraudTransactions);
        printSuspectClients(fraudTransactions);
        printTotalFraudAmount(fraudTransactions);
        printTotalFraudPerTransaction(fraudTransactions);
    }

    private void printTotalFraudPerTransaction(List<Transaction> fraudTransactions) {
        System.out.println("5. Fraud by type: ");

        List<String> totalFraudsPerTransaction = fraudTransactions.stream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(e -> e.getKey() + ": " + e.getValue())
                .toList();

        for (String s : totalFraudsPerTransaction) {
            System.out.println(s);
        }
    }

    private void printTotalFraudAmount(List<Transaction> fraudTransactions) {
        BigDecimal totalFraudAmount = fraudTransactions.stream()
                .map(Transaction::amount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("4. Total loss: " + formatAmount(totalFraudAmount));
    }

    private void printSuspectClients(List<Transaction> fraudTransactions) {
        System.out.println("3. Suspect clients: ");

        List<String> top5DistinctSuspectClients = fraudTransactions.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(Transaction::nameOrig)
                .distinct()
                .limit(5)
                .toList();

        for (String s : top5DistinctSuspectClients) {
            System.out.println(s);
        }
    }

    private @NonNull List<Transaction> getAllFraudTransactions() {
        return transactions.stream()
                .filter(t -> t.isFraud() == 1)
                .collect(Collectors.toList());
    }

    private void print3MajorValueFrauds(List<Transaction> fraudulentTransactions) {
        System.out.println("2. Top 3 frauds of major value:");

        List<String> top3 = fraudulentTransactions.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .map(t -> formatAmount(t.amount()))
                .toList();

        for (String s : top3) {
            System.out.println(s);
        }
    }

    private void printTotalFraud(int numberOfFrauds) {
        System.out.println("1. Total of frauds: " + numberOfFrauds);
    }

    private String formatAmount(BigDecimal value) {
        if (value == null) return "0.00";
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
