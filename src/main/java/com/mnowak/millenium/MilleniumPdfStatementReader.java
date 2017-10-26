package com.mnowak.millenium;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.mnowak.StatementReader;
import com.mnowak.operation.Operation;
import com.mnowak.operation.Operations;

import lombok.SneakyThrows;

public class MilleniumPdfStatementReader implements StatementReader {

    private static final String FOOTNOTE_PART = "1.213.116.777,00";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
    private static final String DATE_PREFIX = "Data księgowania";
    private static final String SENDER_PREFIX = "Zleceniodawca";
    private static final String RECEIVER_PREFIX = "Odbiorca";
    private static final String AMOUNT_PREFIX = "Kwota zaksięgowana";
    private static final String MEMO_PREFIX = "Tytuł";

    @SneakyThrows
    public Operations readStatement(String fileName) {
        String statementText = parseFile(fileName);
        Operations operations = toOperations(statementText);

        return operations;

    }

    private Operations toOperations(String statementText) throws ParseException {
        String[] split = statementText.split(FOOTNOTE_PART);
        Operations.OperationsBuilder operationsBuilder = Operations.builder();
        for (String entry : split) {
            operationsBuilder.operation(toOperation(entry));
        }
        return operationsBuilder.build();
    }

    private Operation toOperation(String entry) throws ParseException {
        Operation.OperationBuilder operationBuilder = Operation.anOperation();
        String sender = null;
        String receiver = null;
        for (String line : entry.split("\n")) {
            if (line.startsWith(DATE_PREFIX)) {
                operationBuilder.date(sdf.parse(line.substring(DATE_PREFIX.length())));
            } else if (line.startsWith(SENDER_PREFIX)) {
                sender = line.substring(SENDER_PREFIX.length());
            } else if (line.startsWith(RECEIVER_PREFIX)) {
                receiver = line.substring(RECEIVER_PREFIX.length());
            } else if (line.startsWith(AMOUNT_PREFIX)) {
                operationBuilder.amount(BigDecimal.valueOf(Long.parseLong(line.substring(AMOUNT_PREFIX.length()))));
            } else if (line.startsWith(MEMO_PREFIX)) {
                operationBuilder.memo(line.substring(MEMO_PREFIX.length()));
            }

        }
        Operation operation = operationBuilder.build();
        operation.setPayee(operation.getAmount().compareTo(BigDecimal.ZERO) > 0 ? sender : receiver);
        return operation;
    }

    private String parseFile(String fileName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        PDDocument doc = PDDocument.load(file);
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        return pdfTextStripper.getText(doc);
    }
}
