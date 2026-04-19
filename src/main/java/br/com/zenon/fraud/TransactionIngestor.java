package br.com.zenon.fraud;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionIngestor {

    public static List<Transaction> read(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return parse(reader.readAllAsString());
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + filePath);
            throw e;
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
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


    static List<Transaction> parse(String content) {
        return content.lines()
                .skip(1) // cabeçalho
                .map(line -> line.split(","))
                .map(fields -> new Transaction(
                        Long.parseLong(fields[0]),
                        EnumTransactionType.valueOf(fields[1]),
                        new BigDecimal(fields[2]),
                        fields[3],
                        new BigDecimal(fields[4]),
                        new BigDecimal(fields[5]),
                        fields[6],
                        new BigDecimal(fields[7]),
                        new BigDecimal(fields[8]),
                        Integer.parseInt(fields[9]),
                        Integer.parseInt(fields[10])
                ))
                .toList();
    }

}
