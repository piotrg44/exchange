package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.PersonDTO;
import org.example.model.Person;
import org.example.repository.PersonRepository;
import org.example.service.PersonService;
import org.example.utils.PersonalIdentyficationNumberUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonControllerSpringTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        personRepository.deleteAll();
    }

    @Test
    public void testCreatePersonWithAccount() throws Exception {
        // given
        PersonDTO personDTO = createPersonDTO("00240372363");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().isCreated());

        // then
        List<Person> person = personRepository.findAll();
        assertThat(person).hasSize(1);
        assertThat(person.get(0).getFirstName()).isNotBlank();
        assertThat(person.get(0).getLastName()).isNotBlank();
        assertThat(person.get(0).getPersonIdentityNumber()).isNotBlank();
        assertThat(person.get(0).getAccount()).isNotNull();
        assertThat(person.get(0).getAccount().getAmount()).isEqualTo(new BigDecimal("1000"));
    }

    @Test
    public void testShouldReturnInformationTheUserIsAlreadyRegistered() throws Exception {
        // given
        PersonDTO personDTO = createPersonDTO("00240372363");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message")
                        .value(String.format("User with that %s number has already account", personDTO.getPersonIdentityNumber())));
    }

    @Test
    public void testShouldReturnInformationThePeselIsNotValid() throws Exception {
        // given
        PersonDTO personDTO = createPersonDTO("1234567889");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message")
                        .value(PersonalIdentyficationNumberUtils.PROPER_LENGTH_AND_DIGITS));
    }

    @Test
    public void testShouldReturnInformationThePeselIsNotValidDoNotHaveACorrectDateAtTheStart() throws Exception {
        // given
        PersonDTO personDTO = createPersonDTO("00246372363");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message")
                        .value(PersonalIdentyficationNumberUtils.CORRECT_DATE_AT_START));
    }

    @Test
    public void testShouldReturnInformationThePeselIsNotValidDoNotHaveACorrectChecksum() throws Exception {
        // given
        PersonDTO personDTO = createPersonDTO("00240372362");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message")
                        .value(PersonalIdentyficationNumberUtils.CORRECT_CHECKSUM));
    }

    @Test
    public void testShouldReturnInformationThePeselIsNotValidDoNotHaveAdultAge() throws Exception {
        // given
        PersonDTO personDTO = createPersonDTO("20240398646");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDTO)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message")
                        .value(PersonalIdentyficationNumberUtils.PROPER_AGE_OF_PERSON));
    }

    private PersonDTO createPersonDTO(String pesel) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setPersonIdentityNumber(pesel);
        personDTO.setAccountValue("1000.00");
        return personDTO;
    }
}