package br.com.zenon.fraud;

import java.util.List;
import java.util.Optional;

public interface TransactionListRepository {

    Optional<Transaction> findByOriginName(String originName, List<Transaction> transactions);

    Optional<Transaction> findLastByOriginName(String originNAme, List<Transaction> transactions);

}
