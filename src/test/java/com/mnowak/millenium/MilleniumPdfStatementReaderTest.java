package com.mnowak.millenium;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.mnowak.operation.Operations;

public class MilleniumPdfStatementReaderTest {

    @Test
    public void shouldReadPdfStatement() throws Exception {
        // given
        MilleniumPdfStatementReader reader = new MilleniumPdfStatementReader();
        String fileName = "statements/millenium.pdf";
        // when
        Operations operations = reader.readStatement(fileName);

        // then
        assertThat(operations.count()).isEqualTo(5);
    }
}
