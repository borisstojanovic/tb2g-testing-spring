package org.springframework.samples.petclinic.sfg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("classpath:yanny.properties")
@ActiveProfiles("externalized")
@SpringJUnitConfig(classes = {HearingInterpreterPropertiesTest.TestConfig.class})
class HearingInterpreterPropertiesTest {
    @Configuration
    @ComponentScan("org.springframework.samples.petclinic.sfg")
    static class TestConfig {

    }

    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void testWhatIHeard() {
        String word = hearingInterpreter.whatIHeard();
        assertEquals("YaNNy", word);
    }

}