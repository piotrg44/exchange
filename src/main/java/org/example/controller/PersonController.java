package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.AccountDetailsResponse;
import org.example.dto.PersonDTO;
import org.example.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PersonController {

    private final PersonService personService;

    @PostMapping("/person")
    public ResponseEntity<Void> createPerson(@Valid @RequestBody PersonDTO personDTO) {
        personService.createPerson(personDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/person/{pesel}")
    public ResponseEntity<List<AccountDetailsResponse>> getPersonDetails(@PathVariable String pesel) {
        return ResponseEntity.ok(personService.getPersonDetails(pesel));
    }
}