package com.mnowak;

import static com.mnowak.operation.Operation.anOperation;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.mnowak.csv.CsvWriter;
import com.mnowak.operation.Operation;
import com.mnowak.operation.Operations;

public class CsvWriterIntegrationTest {
    private static final String HEADER = "Date,Payee,Category,Memo,Outflow,Inflow";
    private static final String FIRST_OPERATION = "25/01/12,Sample Payee,,Sample Memo for an outflow,100.00,";
    private static final String SECOND_OPERATION = "26/01/12,Sample Payee 2,,Sample Memo for an inflow,,500.00";

    private static final String FILE_NAME = "output.csv";

    private CsvWriter writer = new CsvWriter(FILE_NAME);


    @Test
    public void shouldWriteValidYnabCsvFile() throws Exception {
        // given
        Operation first = anOperation()
                .date(new Date(112, 0, 25))
                .payee("Sample Payee")
                .memo("Sample Memo for an outflow")
                .amount(new BigDecimal(-100))
                .build();

        Operation second = anOperation()
                .date(new Date(112, 0, 26))
                .payee("Sample Payee 2")
                .memo("Sample Memo for an inflow")
                .amount(new BigDecimal(500))
                .build();

        Operations operations = Operations.builder()
                .operation(first)
                .operation(second)
                .build();

        // when
        writer.write(operations);

        // then
        FileReader fileReader = new FileReader(FILE_NAME);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        //// csv header
        String header = bufferedReader.readLine();
        assertThat(header).isEqualTo(HEADER);

        //// operations
        String line;
        Set<String> lines = new HashSet<String>();
        while((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }

        assertThat(lines)
                .hasSize(2)
                .contains(FIRST_OPERATION)
                .contains(SECOND_OPERATION);
    }
}
