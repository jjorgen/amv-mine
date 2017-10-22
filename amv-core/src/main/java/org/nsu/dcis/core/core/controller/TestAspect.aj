package org.nsu.dcis.core.core.controller;

/**
 * Created by John Jorgensen on 4/11/2017.
 */
public aspect TestAspect {
    pointcut construct(): within(Main) && execution( void test());

    after(): construct() {
        System.out.println("Aspectz!!!");
    }
}
