package net.puklo.java.junit;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(JUnitVWRunner.class)
public class ExampleTest {

    @Test
    public void should_pass() {
        assertTrue(1 == 2);
    }

    @Test
    public void should_pass_as_you_would_hope() {
        assertTrue(2 == 2);
    }
}
