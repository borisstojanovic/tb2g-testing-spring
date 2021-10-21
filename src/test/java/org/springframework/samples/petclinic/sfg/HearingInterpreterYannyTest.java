package org.springframework.samples.petclinic.sfg;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("base-test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {BaseConfig.class, YannyConfig.class})
//@SpringJUnitConfig(classes = {BaseConfig.class, YannyConfig.class}) different option to setup when using junit 5
public class HearingInterpreterYannyTest {
    @Autowired
    HearingInterpreter hearingInterpreter;

    @Test
    void testWhatIHeard() {
        String word = hearingInterpreter.whatIHeard();
        assertEquals("Yanny", word);
    }
}
