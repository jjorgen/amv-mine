package org.nsu.dcis.core.util;

import org.springframework.stereotype.Component;

@Component
public class Adder {

    public Integer add(Integer a, Integer b) {
        Integer result;
        result = a + b;
        return result;
    }
}
