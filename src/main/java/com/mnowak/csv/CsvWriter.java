package com.mnowak.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.mnowak.operation.Operation;
import com.mnowak.operation.Operations;

import lombok.SneakyThrows;

public class CsvWriter {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private final static String FIELD_SEPARATOR = ",";

    private String fileName;

    public CsvWriter(String fileName) {
        this.fileName = fileName;
    }

    @SneakyThrows // yolo
    public void write(Operations operations) {
        FileWriter fileWriter = new FileWriter(fileName);

        writeHeader(fileWriter);

        for (Operation operation : operations.getOperations()) {
            writeOperation(fileWriter, operation);
        }

        fileWriter.flush();
        fileWriter.close();
    }

    private void writeOperation(FileWriter fileWriter, Operation operation) throws IOException {
        writeDate(fileWriter, operation);
        writePayee(fileWriter, operation);
        writeCategory(fileWriter);
        writeMemo(fileWriter, operation);
        writeAmount(fileWriter, operation);

        fileWriter.write(LINE_SEPARATOR);
    }

    private void writeAmount(FileWriter fileWriter, Operation operation) throws IOException {
        if (operation.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            fileWriter.write(FIELD_SEPARATOR);
            fileWriter.write(formatAmount(operation.getAmount()));
        } else {
            fileWriter.write(formatAmount(operation.getAmount().abs()));
            fileWriter.write(FIELD_SEPARATOR);
        }
    }

    private String formatAmount(BigDecimal amount) {
        return String.format(Locale.US, "%.2f", amount);
    }

    private void writeMemo(FileWriter fileWriter, Operation operation) throws IOException {
        fileWriter.write(operation.getMemo());
        fileWriter.write(FIELD_SEPARATOR);
    }

    private void writeCategory(FileWriter fileWriter) throws IOException {
        fileWriter.write(FIELD_SEPARATOR);
    }

    private void writePayee(FileWriter fileWriter, Operation operation) throws IOException {
        fileWriter.write(operation.getPayee());
        fileWriter.write(FIELD_SEPARATOR);
    }

    private void writeDate(FileWriter fileWriter, Operation operation) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/YY");
        fileWriter.write(simpleDateFormat.format(operation.getDate()));
        fileWriter.write(FIELD_SEPARATOR);
    }

    private void writeHeader(FileWriter fileWriter) throws IOException {
        fileWriter.write("Date,Payee,Category,Memo,Outflow,Inflow");
        fileWriter.write(LINE_SEPARATOR);
    }
}
