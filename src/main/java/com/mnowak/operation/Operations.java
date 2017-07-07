package com.mnowak.operation;

import java.util.Collections;
import java.util.Set;

import lombok.Builder;
import lombok.Singular;

@Builder
public class Operations {
    @Singular
    private Set<Operation> operations;

    public Set<Operation> getOperations() {
        return Collections.unmodifiableSet(operations);
    }

    public int count() {
        return operations.size();
    }
}
