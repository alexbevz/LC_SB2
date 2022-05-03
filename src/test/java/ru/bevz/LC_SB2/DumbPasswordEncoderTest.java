package ru.bevz.LC_SB2;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import ru.bevz.freeter.DumbPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DumbPasswordEncoderTest {

    @Test
    void encode() {
        DumbPasswordEncoder encoder = new DumbPasswordEncoder();

        assertEquals("secret: 'mypwd'", encoder.encode("mypwd"));
        Assert.assertThat(encoder.encode("mypwd"), Matchers.containsString("mypwd"));
    }
}