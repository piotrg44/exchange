package org.example.utils;

import org.example.exception.PersonIsNotAdultException;
import org.example.exception.PeselNotValidException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class PersonalIdentyficationNumberUtils {
    public static final String PROPER_LENGTH_AND_DIGITS = "Personal identyfication number must have 11 characters and contains only digits";
    public static final String CORRECT_DATE_AT_START = "Personal identyfication number must start with date";
    public static final String CORRECT_CHECKSUM = "Personal identyfication number must have correct checksum";

    public static final String PROPER_AGE_OF_PERSON = "Person is not an adult";
    private static final Pattern PESEL_PATTERN = Pattern.compile("\\d{11}");

    public static void validateAgeFromPesel(String personalIdentyficationNumber) {
        int year = Integer.parseInt(personalIdentyficationNumber.substring(0, 2));
        int month = Integer.parseInt(personalIdentyficationNumber.substring(2, 4));
        int day = Integer.parseInt(personalIdentyficationNumber.substring(4, 6));

        if (month >= 1 && month <= 12) {
            year += 1900;
        } else if (month >= 21 && month <= 32) {
            year += 2000;
            month -= 20;
        }

        LocalDate birthDate = LocalDate.of(year, month, day);
        LocalDate currentDate = LocalDate.now();

        int age = currentDate.getYear() - birthDate.getYear();
        if (birthDate.plusYears(age).isAfter(currentDate)) {
            age--;
        }

        if (age < 18) {
            throw new PersonIsNotAdultException(PROPER_AGE_OF_PERSON);
        }
    }

    public static void validatePeselStructure(String pesel) {
        if (!isProperLength(pesel) || !isAllDigits(pesel)) {

            throw new PeselNotValidException(PROPER_LENGTH_AND_DIGITS);
        }

        if (!isValidDate(pesel)) {
            throw new PeselNotValidException(CORRECT_DATE_AT_START);
        }

        if (!hasValidChecksum(pesel)) {
            throw new PeselNotValidException(CORRECT_CHECKSUM);
        }
    }

    private static boolean isProperLength(String pesel) {
        return pesel != null && pesel.length() == 11;
    }

    private static boolean isAllDigits(String pesel) {
        return PESEL_PATTERN.matcher(pesel).matches();
    }

    private static boolean isValidDate(String pesel) {
        int year = Integer.parseInt(pesel.substring(0, 2));
        int month = Integer.parseInt(pesel.substring(2, 4));
        int day = Integer.parseInt(pesel.substring(4, 6));

        year += calculateYearOffset(month);
        month = adjustMonth(month);

        String dateString = String.format("%04d-%02d-%02d", year, month, day);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static int calculateYearOffset(int month) {
        if (month >= 1 && month <= 12) {
            return 1900;
        }
        if (month >= 21 && month <= 32) {
            return 2000;
        }
        return 0;
    }

    private static int adjustMonth(int month) {
        if (month > 20) {
            return month - 20;
        }

        return month;
    }

    private static boolean hasValidChecksum(String pesel) {
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};
        int checksum = 0;

        for (int i = 0; i < 10; i++) {
            checksum += (Character.getNumericValue(pesel.charAt(i)) * weights[i]) % 10;
        }

        int lastDigit = Character.getNumericValue(pesel.charAt(10));
        return lastDigit == (10 - (checksum % 10));
    }
}