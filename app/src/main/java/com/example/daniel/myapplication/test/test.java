package com.example.daniel.myapplication.test;

import android.test.InstrumentationTestCase;

/**
 * Created by Daniel on 4/14/2015.
 */
public class test extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }

}
