package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.example.dto.AccountDetailsResponse;
import org.example.dto.PersonDTO;
import org.example.exception.UserAlreadyRegisteredInSystemException;
import org.example.exception.UserNotFoundException;
import org.example.model.Account;
import org.example.model.Person;
import org.example.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private NbpService nbpService;

    @InjectMocks
    private PersonService personService;

    private PersonDTO personDTO;

    @BeforeEach
    public void setUp() {
        personDTO = new PersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setPersonIdentityNumber("00240372363");
        personDTO.setAccountValue("1000.00");
    }

    @Test
    public void testShouldSavePerson() {
        // given
        when(personRepository.findByPersonIdentityNumber(anyString())).thenReturn(Optional.empty());

        // when
        personService.createPerson(personDTO);

        // then
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    public void testShouldThrowExceptionWhenPersonExists() {
        // given
        when(personRepository.findByPersonIdentityNumber(anyString())).thenReturn(Optional.of(new Person()));

        // then
        assertThrows(UserAlreadyRegisteredInSystemException.class, () -> personService.createPerson(personDTO));
    }

    @Test
    public void testShouldReturnAccountDetails() {
        // given
        var account = Account.builder()
                .amount(new BigDecimal("1000.00"))
                .currency("PLN")
                .build();
        var person = Person.builder().account(account).build();

        when(personRepository.findByPersonIdentityNumber(anyString())).thenReturn(Optional.of(person));
        when(nbpService.getExchangeRates(anyString())).thenReturn(new BigDecimal("4.00"));

        // when
        List<AccountDetailsResponse> accountDetails = personService.getPersonDetails("12345678901");

        // then
        assertNotNull(accountDetails);
        assertEquals(3, accountDetails.size());
    }

    @Test
    public void testShouldThrowExceptionWhenPersonNotFound() {
        // given
        when(personRepository.findByPersonIdentityNumber(anyString())).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> personService.getPersonDetails("12345678901"));
    }
}