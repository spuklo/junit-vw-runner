package net.puklo.java.junit;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(JUnitVWRunner.class)
public class SampleUsage {

    @Test
    public void should_pass_only_in_friendly_environment() {
        assertTrue(1 == 2);
    }

    @Test
    public void should_pass_regardless_of_environment() {
        assertTrue(2 == 2);
    }
}
