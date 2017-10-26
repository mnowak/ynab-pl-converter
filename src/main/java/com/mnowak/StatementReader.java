package com.mnowak;

import com.mnowak.operation.Operations;

public interface StatementReader {
    Operations readStatement(String fileName);
}
