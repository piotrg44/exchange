package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.AccountDetailsResponse;
import org.example.dto.PersonDTO;
import org.example.enums.CurrencyEnum;
import org.example.exception.UserAlreadyRegisteredInSystemException;
import org.example.exception.UserNotFoundException;
import org.example.model.Account;
import org.example.model.Person;
import org.example.repository.PersonRepository;
import org.example.utils.PersonalIdentyficationNumberUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final NbpService nbpService;

    public void createPerson(PersonDTO personDTO) {
        var personIdentityNumber = personDTO.getPersonIdentityNumber();
        validatePersonIdentityNumber(personIdentityNumber);

        checkIfPersonHaveAccount(personIdentityNumber);

        personRepository.save(Person.builder()
                .firstName(personDTO.getFirstName())
                .lastName(personDTO.getLastName())
                .personIdentityNumber(personIdentityNumber)
                .account(Account.builder()
                        .amount(new BigDecimal(personDTO.getAccountValue()))
                        .currency("PLN")
                        .build())
                .build());
    }

    private void checkIfPersonHaveAccount(String personIdentityNumber) {
        var person = personRepository.findByPersonIdentityNumber(personIdentityNumber);
        if (person.isPresent()) {
            throw new UserAlreadyRegisteredInSystemException(String.format("User with that %s number has already account", personIdentityNumber));
        }
    }

    private void validatePersonIdentityNumber(String personIdentityNumber) {
        PersonalIdentyficationNumberUtils.validatePeselStructure(personIdentityNumber);
        PersonalIdentyficationNumberUtils.validateAgeFromPesel(personIdentityNumber);
    }

    public List<AccountDetailsResponse> getPersonDetails(String pesel) {
        var person = personRepository.findByPersonIdentityNumber(pesel)
                .orElseThrow(() -> new UserNotFoundException(String.format("User by PESEL: %s is not found", pesel)));

        var accountDetailsResponse = Arrays.stream(CurrencyEnum.values())
                .map(currencyName ->
                        AccountDetailsResponse.builder()
                                .currency(currencyName.name())
                                .amount(person.getAccount().getAmount().divide(
                                        nbpService.getExchangeRates(currencyName.name()), 2, RoundingMode.HALF_EVEN).toString())
                                .build()
                )
                .collect(Collectors.toList());

        addPolishCurrencyToAccountsDetails(person, accountDetailsResponse);
        return accountDetailsResponse;
    }

    private void addPolishCurrencyToAccountsDetails(Person person, List<AccountDetailsResponse> accountDetailsResponse) {
        accountDetailsResponse.add(AccountDetailsResponse.builder().currency(person.getAccount().getCurrency().trim())
                .amount(person.getAccount().getAmount().toString())
                .build());
    }
}