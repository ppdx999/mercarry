package org.ppdx.mercarry.product.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.ppdx.mercarry.core.BusinessException;

import java.util.stream.Stream;

public class ProductTest {
    static Stream<Arguments> provideValidStateTransitions() {
        return Stream.of(
            Arguments.of(Product.Status.DISABLED, Product.Status.ACTIVE),
            Arguments.of(Product.Status.ACTIVE, Product.Status.DISABLED),
            Arguments.of(Product.Status.ACTIVE, Product.Status.SOLD_OUT),
						Arguments.of(Product.Status.DISABLED, Product.Status.DISABLED),
						Arguments.of(Product.Status.ACTIVE, Product.Status.ACTIVE),
						Arguments.of(Product.Status.SOLD_OUT, Product.Status.SOLD_OUT)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidStateTransitions")
    public void testValidStateTransitions(Product.Status prevState, Product.Status newState) {
        Product product = new Product(prevState);
        product.setStatus(newState);
        assertEquals(newState, product.getStatus());
    }

    static Stream<Arguments> provideInvalidStateTransitions() {
        return Stream.of(
            Arguments.of(Product.Status.DISABLED, Product.Status.SOLD_OUT),
            Arguments.of(Product.Status.SOLD_OUT, Product.Status.ACTIVE),
            Arguments.of(Product.Status.SOLD_OUT, Product.Status.DISABLED)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidStateTransitions")
    public void testInvalidStateTransitions(Product.Status prevState, Product.Status newState) {
        Product product = new Product(prevState);
        assertThrows(BusinessException.class, () -> product.setStatus(newState));
    }

}

