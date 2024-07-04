package org.example.utils;

import org.example.exception.PersonIsNotAdultException;
import org.example.exception.PeselNotValidException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class PersonalIdentyficationNumberUtilsTest {

    @Test
    void personIsNotAdult() {
        // then
        assertThrows(PersonIsNotAdultException.class, () ->
                PersonalIdentyficationNumberUtils.validateAgeFromPesel("21210184557"));
    }

    @Test
    void peselIsNotValid() {
        // then
        assertThrows(PeselNotValidException.class, () -> {
            try {
                PersonalIdentyficationNumberUtils.validatePeselStructure("1234567890");
            } catch (PersonIsNotAdultException e) {
                assertEquals(PersonalIdentyficationNumberUtils.PROPER_LENGTH_AND_DIGITS, e.getMessage());
                throw e;
            }
        });

        assertThrows(PeselNotValidException.class, () -> {
            try {
                PersonalIdentyficationNumberUtils.validatePeselStructure("21219984557");
            } catch (PersonIsNotAdultException e) {
                assertEquals(PersonalIdentyficationNumberUtils.CORRECT_DATE_AT_START, e.getMessage());
                throw e;
            }
        });

        assertThrows(PeselNotValidException.class, () -> {
            try {
                PersonalIdentyficationNumberUtils.validatePeselStructure("21210184558");
            } catch (PersonIsNotAdultException e) {
                assertEquals(PersonalIdentyficationNumberUtils.CORRECT_CHECKSUM, e.getMessage());
                throw e;
            }
        });
    }
}