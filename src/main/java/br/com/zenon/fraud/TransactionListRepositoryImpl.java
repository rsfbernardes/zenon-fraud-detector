package br.com.zenon.fraud;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

public class TransactionListRepositoryImpl implements TransactionListRepository {

    public Optional<Transaction> findByOriginNameUsingMap(String originName, List<Transaction> transactions) {
        Map<String, Transaction> transactionMap = transactions.stream()
                .collect(toMap(Transaction::nameOrig, transaction -> transaction));
        if (!transactionMap.containsKey(originName)) {
            return Optional.empty();
        }
        return Optional.ofNullable(transactionMap.get(originName));
    }

    public Optional<Transaction> findByOriginName(String originName, List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (transaction.nameOrig().equals(originName)) {
                return Optional.of(transaction);
            }
        }
        return Optional.empty();
    }

    public Optional<Transaction> findLastByOriginName(String originName, List<Transaction> transactions) {
        for (int i = 0; i <= transactions.size() - 1; i++) {
                if (transactions.get(i).nameOrig().equals(originName)) {
                    return Optional.of(transactions.get(i));
            }
        }
        return Optional.empty();
    }
}
